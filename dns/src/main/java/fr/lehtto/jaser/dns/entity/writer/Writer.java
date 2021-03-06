package fr.lehtto.jaser.dns.entity.writer;

import fr.lehtto.jaser.dns.entity.Writable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Writes a {@link Writable writable} to a buffer.
 *
 * @author Lehtto
 * @since 0.1.0
 */
final class Writer {

  /**
   * Default constructor.
   */
  private Writer() {
    throw new AssertionError("This constructor should not be called");
  }

  /**
   * Writes a {@link Writable writable} to a buffer.
   *
   * @param writable the {@link Writable writable} to write to the buffer
   * @param buffer the buffer to write to
   * @param offset the offset to start writing at
   * @return the number of bytes written
   */
  @Contract(mutates = "param2")
  static int write(final @NotNull Writable writable, final byte @NotNull [] buffer, final int offset) {
    final byte[] bytes = writable.getBytes();
    System.arraycopy(bytes, 0, buffer, offset, bytes.length);
    return bytes.length;
  }

}
