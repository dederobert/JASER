package fr.lehtto.jaser.dns.query.handler;

import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Response;
import org.jetbrains.annotations.NotNull;

/**
 * {@link QueryHandler Query handler} for A (IPv4) query.
 *
 * @author lehtto
 * @since  0.1.0
 * @version 0.2.0
 */
final class DefaultQueryHandler implements QueryHandler {

  static final DefaultQueryHandler INSTANCE = new DefaultQueryHandler();

  /**
   * Default constructor.
   */
  private DefaultQueryHandler() {
    // This constructor should not be called
  }

  @Override
  public @NotNull Response handleValidatedQuery(final @NotNull Query query) {
    LOG.warn("Unsupported query type: {}", query.questions().get(0).type());
    return QueryHandlerHelper.INSTANCE.newNotImplementedResponse(query);
  }
}
