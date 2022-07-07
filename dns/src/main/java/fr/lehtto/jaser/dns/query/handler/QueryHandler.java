package fr.lehtto.jaser.dns.query.handler;

import fr.lehtto.jaser.dns.QueryValidator;
import fr.lehtto.jaser.dns.QueryValidator.QueryValidationResult;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Response;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * DNS query handler.
 *
 * @author lehtto
 * @version 0.1.0
 */
@FunctionalInterface
public interface QueryHandler {

  Logger LOG = LogManager.getLogger(QueryHandler.class);

  /**
   * Handles the given query.
   * <ul>
   *   <li>Validates the query</li>
   *   <li>Creates the response</li>
   * </ul>
   *
   * @param query the query to handle
   * @return the response
   */
  default Response handle(final @NotNull Query query) {
    // Validate the query
    final QueryValidationResult validationResult = QueryValidator.validate(query);

    if (!validationResult.valid()) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Query is invalid: {}", validationResult.message());
      }
      // Invalid query => return error response with RCODE=FORMAT_ERROR
      return Response.builder()
          .header(query.header().toBuilder()
              .flags(query.header().flags().toBuilder().qr(QR.RESPONSE)
                  .rcode(RCode.FORMAT_ERROR)
                  .build())
              .build())
          .questions(query.questions())
          .noAnswer()
          .noAuthorityRecords()
          .noAdditionalRecords()
          .build();
    }

    // Current implementation only handles queries with one question
    if (1 != query.header().qdcount()) {
      LOG.info("Query has more than one question");
      // Invalid query => return error response with RCODE=NOT_IMPLEMENTED
      return Response.builder()
          .header(query.header().toBuilder()
              .flags(query.header().flags().toBuilder().qr(QR.RESPONSE)
                  .rcode(RCode.NOT_IMPLEMENTED)
                  .build())
              .build())
          .questions(query.questions())
          .noAnswer()
          .noAuthorityRecords()
          .noAdditionalRecords()
          .build();
    }

    return handleValidatedQuery(query);
  }

  /**
   * Handles the given {@link Query query} after it has been validated.
   *
   * @param query the query to handle
   * @return the response to the query
   */
  @OverrideOnly
  @NotNull Response handleValidatedQuery(final @NotNull Query query);

}
