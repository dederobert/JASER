package fr.lehtto.jaser.dns.query.handler;

import fr.lehtto.jaser.dns.Dns;
import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Question;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.ResourceRecord.Builder;
import fr.lehtto.jaser.dns.entity.Response;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import fr.lehtto.jaser.dns.entity.rdata.MultiNamedRData;
import fr.lehtto.jaser.dns.entity.rdata.NamedRData;
import fr.lehtto.jaser.dns.master.file.Zone;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Helper class for {@link QueryHandler}.
 *
 * @author lehtto
 * @version 1.0.0
 */
final class QueryHandlerHelper {

  public static final QueryHandlerHelper INSTANCE = new QueryHandlerHelper();

  /**
   * Default constructor.
   */
  private QueryHandlerHelper() {
    // This constructor should not be called
  }

  /**
   * Search in zone for the given question.
   *
   * @param question the question to search for
   * @return the list of resource records found
   */
  @Unmodifiable
  @NotNull List<ResourceRecord> search(final @NotNull Question question) {
    return Dns.INSTANCE.getMasterFile()
        .search(question.name())
        .stream()
        .map(Zone::getRecords)
        .flatMap(List::stream)
        .filter(resourceRecord -> question.type() == resourceRecord.type())
        .map(ResourceRecord::toBuilder)
        .map(builder -> builder.pointer(question))
        .map(Builder::build)
        .toList();
  }

  /**
   * Resolves additional records for the given question.
   *
   * @param question the question to resolve additional records for
   * @param answers  the list of answers to add additional records to
   * @return the list of additional records found
   */
  @Unmodifiable
  @NotNull List<ResourceRecord> resolveAdditionalRecords(final @NotNull Question question,
      final @NotNull List<ResourceRecord> answers) {
    final ArrayList<ResourceRecord> result = new ArrayList<>();
    for (final ResourceRecord answer : answers) {
      if (answer.data() instanceof NamedRData namedRData) {
        search(namedRData.getName(), answer).forEach(result::add);
      } else if (answer.data() instanceof MultiNamedRData namedRData) {
        for (final DomainName name : namedRData.getNames()) {
          search(name, answer).forEach(result::add);
        }
      }
    }
    return List.copyOf(result);
  }

  /**
   * Creates a response with not implemented error.
   *
   * @param query the query to answer
   * @return the response
   */
  Response newNotImplementedResponse(final @NotNull Query query) {
    return Response.builder()
        .header(query.header()
            .toBuilder()
            .flags(query.header()
                .flags()
                .toBuilder()
                .qr(QR.RESPONSE)
                .rcode(RCode.NOT_IMPLEMENTED)
                .build())
            .build())
        .questions(query.questions())
        .noAnswer()
        .noAuthorityRecords()
        .noAdditionalRecords()
        .build();
  }

  /**
   * Search in zone for the given name.
   *
   * @param name    the name to search for
   * @param pointer the pointer to use for the resource records
   * @return the list of resource records found
   */
  private Stream<ResourceRecord> search(final @NotNull DomainName name, final @NotNull Object pointer) {
    return Dns.INSTANCE.getMasterFile()
        .search(name)
        .stream()
        .map(Zone::getRecords)
        .flatMap(List::stream)
        .map(ResourceRecord::toBuilder)
        .map(builder -> builder.pointer(pointer))
        .map(Builder::build);
  }
}
