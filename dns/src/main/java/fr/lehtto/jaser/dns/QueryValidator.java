package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import org.jetbrains.annotations.NotNull;

/**
 * Validator for DNS queries.
 *
 * @author lehtto
 * @version 0.1.0
 */
public final class QueryValidator {

  /**
   * Default constructor.
   */
  private QueryValidator() {
    throw new AssertionError("This constructor should not be called");
  }

  /**
   * Validates the given {@link Query query}.
   *
   * @param query the query to validate
   * @return the validation result
   */
  public static @NotNull QueryValidationResult validate(final @NotNull Query query) {
    // Verify that the query has QR bit set to 0
    if (QR.QUERY != query.header().flags().qr()) {
      return QueryValidationResult.invalid("The query's QR bit must be set to 0 (QUERY)");
    }

    // Verify that the query question count is equal to question count in the header
    final short qdcount = query.header().qdcount();
    final int questionsSize = query.questions().size();

    if (qdcount != questionsSize) {
      return QueryValidationResult.invalid("Question count in header is not equal to question count in query");
    }

    return QueryValidationResult.valid("Query is valid");
  }

  /**
   * Validation result.
   */
  public static record QueryValidationResult(boolean valid, @NotNull String message) {

    /**
     * Creates invalid query validation result.
     *
     * @param message the message to set
     * @return the query validation result
     */
    static QueryValidationResult invalid(final @NotNull String message) {
      return new QueryValidationResult(false, message);
    }

    /**
     * Creates valid query validation result.
     *
     * @param message the message to set
     * @return the query validation result
     */
    static QueryValidationResult valid(final @NotNull String message) {
      return new QueryValidationResult(true, message);
    }
  }
}
