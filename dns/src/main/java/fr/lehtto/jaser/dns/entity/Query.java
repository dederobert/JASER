package fr.lehtto.jaser.dns.entity;

import static fr.lehtto.jaser.dns.entity.Header.HEADER_SIZE;

import fr.lehtto.jaser.dns.entity.parser.HeaderParser;
import fr.lehtto.jaser.dns.entity.parser.QuestionParser;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * DNS query.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Query(@NotNull Header header, @NotNull List<Question> questions) {

  private static final Logger LOG = LogManager.getLogger(Query.class);


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
}
