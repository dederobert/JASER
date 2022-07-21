package fr.lehtto.jaser.dns.entity.writer;

import fr.lehtto.jaser.dns.entity.Question;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes a DNS response to a buffer.
 *
 * @author Lehtto
 * @version 0.2.0
 * @since 0.1.0
 */
public final class ResponseWriter {

  private static final Logger LOG =
      LoggerFactory.getLogger(ResponseWriter.class);

  /**
   * Default constructor.
   */
  private ResponseWriter() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Writes a DNS response to a buffer.
   *
   * @param response the response to write to the buffer
   * @param buffer   the buffer to write to
   * @return the number of bytes written
   */
  @Contract(mutates = "param2")
  public static int write(final @NotNull Response response,
                          final byte @NotNull[] buffer) {
    LOG.debug("Write response");

    response.resolvePointers();

    // Write the header
    int i = Writer.write(response.header(), buffer, 0);

    // Write the questions
    for (final Question question : response.questions()) {
      i += Writer.write(question, buffer, i);
    }

    // Write the answerRecords
    for (final ResourceRecord answer : response.answerRecords()) {
      i += Writer.write(answer, buffer, i);
    }

    // Write the authorityRecords
    for (final ResourceRecord authority : response.authorityRecords()) {
      i += Writer.write(authority, buffer, i);
    }

    // Write the additionalRecords
    for (final ResourceRecord additional : response.additionalRecords()) {
      i += Writer.write(additional, buffer, i);
    }
    return i;
  }
}
