package fr.lehtto.jaser.dns.query.handler;

import fr.lehtto.jaser.dns.entity.Query;
import org.jetbrains.annotations.NotNull;

/**
 * DNS query handler factory.
 *
 * @author lehtto
 * @version 0.1.0
 */
public final class QueryHandlerFactory {

  /**
   * Default constructor.
   */
  private QueryHandlerFactory() {
    throw new AssertionError("This constructor should not be called");
  }

  /**
   * Create a new {@link QueryHandler Query handler} for the given {@link Query query}.
   *
   * @param query the query to handle
   * @return the handler for the query
   */
  public static QueryHandler fromQuery(final @NotNull Query query) {
    // Current implementation only handles 1 question per query (check type of first question)
    return switch (query.questions().get(0).type()) {
      case A -> AQueryHandler.INSTANCE;
      default -> throw new IllegalArgumentException("Unsupported query type");
    };
  }
}
