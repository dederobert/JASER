package fr.lehtto.jaser.dns.entity.writer;

import com.jcabi.aspects.Loggable;
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
 * @version 1.0.0
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
  @Loggable(Loggable.DEBUG)
  public static int write(final @NotNull Response response,
      final byte @NotNull [] buffer) {
    LOG.debug("Write response");

    // Write the header
    int i = Writer.write(response.header(), buffer, 0);

    // Write the questions
    for (final Question question : response.questions()) {
      i += Writer.write(question, buffer, i);
    }

    // Write the answerRecords
    for (final ResourceRecord answer : response.answerRecords()) {
      resolvePointers(answer, buffer, i);
      i += Writer.write(answer, buffer, i);
    }

    // Write the authorityRecords
    for (final ResourceRecord authority : response.authorityRecords()) {
      resolvePointers(authority, buffer, i);
      i += Writer.write(authority, buffer, i);
    }

    // Write the additionalRecords
    for (final ResourceRecord additional : response.additionalRecords()) {
      resolvePointers(additional, buffer, i);
      i += Writer.write(additional, buffer, i);
    }

    return i;
  }

  /**
   * Resolves pointers in a {@link ResourceRecord}.
   *
   * @param answer the answer to resolve pointers for
   * @param buffer the current buffer
   * @param i      the current index in the buffer
   * @since 1.0.0
   */
  private static void resolvePointers(final ResourceRecord answer, final byte[] buffer, final int i) {
    if (!answer.useCompression()) {
      return;
    }
    assert null != answer.pointer() : "Compression is enabled but pointer is null";
    final byte[] pointerBytes = answer.pointer().getBytes();
    final int pointerBytesLength = pointerBytes.length;

    for (int j = 0; j < i; j++) {
      if (buffer[j] == pointerBytes[0]) {
        boolean match = true;
        for (int k = 0; k < pointerBytesLength; k++) {
          if (buffer[j + k] != pointerBytes[k]) {
            // Not matching
            match = false;
            break;
          }
        }
        if (match) {
          // We found the pointer
          answer.pointerShort(j);
          break;
        }
      }
    }
  }
}
