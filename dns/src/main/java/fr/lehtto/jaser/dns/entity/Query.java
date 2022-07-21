package fr.lehtto.jaser.dns.entity;

import static fr.lehtto.jaser.dns.entity.Header.HEADER_SIZE;

import fr.lehtto.jaser.dns.entity.parser.HeaderParser;
import fr.lehtto.jaser.dns.entity.parser.QuestionParser;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DNS query.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Query(@NotNull Header header, @NotNull List<Question> questions) {

  private static final Logger LOG = LoggerFactory.getLogger(Query.class);


  /**
   * Reads a DNS query from a socket.
   *
   * @param bytes  the bytes to read
   * @param length the length of the bytes to read
   * @return the query
   */
  public static @NotNull Query read(final byte[] bytes, final int length) {
    LOG.trace("Read {} bytes", length);

    // Read the header
    final Header header = HeaderParser.parse(Arrays.copyOfRange(bytes, 0, HEADER_SIZE));
    // Read the questions
    final List<Question> questions = QuestionParser.parse(bytes, HEADER_SIZE, length);

    return new Query(header, questions);
  }

  /**
   * Creates Builder for the query.
   *
   * @return the builder
   */
  public static @NotNull Builder builder() {
    return new Builder();
  }

  /**
   * Builder for the query.
   *
   * @return the builder
   */
  public @NotNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * Builder for the query.
   */
  public static final class Builder {

    private @Nullable Header header;
    private @Nullable List<Question> questions;

    /**
     * Default constructor.
     */
    private Builder() {
      header = null;
      questions = null;
    }

    /**
     * Constructor for the builder.
     *
     * @param query the query to copy
     */
    private Builder(final @NotNull Query query) {
      header = query.header;
      questions = query.questions;
    }

    /**
     * Sets the header.
     *
     * @param header the header
     * @return the builder
     */
    public @NotNull Builder header(final @NotNull Header header) {
      this.header = header;
      return this;
    }

    /**
     * Sets the questions.
     *
     * @param questions the questions
     * @return the builder
     */
    public @NotNull Builder questions(final @NotNull List<Question> questions) {
      this.questions = questions;
      return this;
    }

    /**
     * Builds the query.
     *
     * @return the query
     */
    public @NotNull Query build() {
      if (null == header) {
        throw new IllegalStateException("Header is not set");
      }
      if (null == questions) {
        throw new IllegalStateException("Questions is not set");
      }
      return new Query(header, questions);
    }
  }
}
