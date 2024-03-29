package fr.lehtto.jaser.dns.query.handler;

import fr.lehtto.jaser.dns.Dns;
import fr.lehtto.jaser.dns.entity.Flags;
import fr.lehtto.jaser.dns.entity.Header;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Question;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.Response;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import fr.lehtto.jaser.dns.metrics.MetricsService;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Query handler for NS queries.
 *
 * @author lehtto
 * @since 0.2.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
final class NSQueryHandler implements QueryHandler {

  static final QueryHandler INSTANCE = new NSQueryHandler();

  /**
   * Default constructor.
   */
  private NSQueryHandler() {
    // This constructor should not be called
  }

  @Override
  public @NotNull Response handleValidatedQuery(final @NotNull Query query) {
    Dns.INSTANCE.getMetricsService()
        .map(MetricsService::getMetrics)
        .ifPresent(metrics -> metrics.incrementNsQuery(1));
    // Current implementation only supports one question
    final Question question = query.questions().get(0);

    final List<ResourceRecord> answers = QueryHandlerHelper.INSTANCE.search(question);
    final List<ResourceRecord> additionalRecords = QueryHandlerHelper.INSTANCE.resolveAdditionalRecords(question,
        answers);

    // Create response header's flags
    final Flags flags = query.header()
                            .flags()
                            .toBuilder()
                            .qr(QR.RESPONSE)
                            .rcode(RCode.NO_ERROR)
                            .build();

    // Create response's header
    final Header header = query.header().toBuilder()
        // Set the answer count to 1
        .ancount((short) answers.size())
        .arcount((short) additionalRecords.size())
        .flags(flags)
        .build();

    // Create response
    return Response.builder()
        .header(header)
        .questions(query.questions())
        .answers(answers)
        .noAuthorityRecords()
        .additionalRecords(additionalRecords)
        .build();
  }
}
