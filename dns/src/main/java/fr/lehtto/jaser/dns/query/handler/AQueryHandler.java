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
import fr.lehtto.jaser.dns.master.file.MasterFileQuerier;
import fr.lehtto.jaser.dns.metrics.MetricsService;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * {@link QueryHandler Query handler} for A (IPv4) query.
 *
 * @author lehtto
 * @since  0.1.0
 * @version 0.2.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
final class AQueryHandler implements QueryHandler {

  static final AQueryHandler INSTANCE = new AQueryHandler();

  /**
   * Default constructor.
   */
  private AQueryHandler() {
    // This constructor should not be called
  }

  @Override
  public @NotNull Response handleValidatedQuery(final @NotNull Query query) {
    Dns.INSTANCE.getMetricsService().map(MetricsService::getMetrics).ifPresent(metrics -> metrics.incrementAQuery(1));
    // Current implementation only supports one question
    final Question question = query.questions().get(0);

    final List<ResourceRecord> answers = new MasterFileQuerier(Dns.INSTANCE.getMasterFiles())
        .withClass(question.recordClass())
        .withType(question.type())
        .withDomain(question.name())
        .getRecordsStream()
        .map(resourceRecord -> ResourceRecord.builder()
            .pointer(question)
            .type(resourceRecord.type())
            .recordClass(resourceRecord.recordClass())
            .ttl(resourceRecord.ttl())
            .data(resourceRecord.data())
            .build())
        .toList();

    // Create response header's flags
    final Flags flags = query.header().flags().toBuilder().qr(QR.RESPONSE)
        .rcode(RCode.NO_ERROR)
        .build();

    // Create response's header
    final Header header = query.header().toBuilder()
        // Set the answer count to 1
        .ancount((short) answers.size())
        .flags(flags)
        .build();

    // Create response
    return Response.builder()
        .header(header)
        .questions(query.questions())
        .answers(answers)
        .noAuthorityRecords()
        .noAdditionalRecords()
        .build();
  }
}
