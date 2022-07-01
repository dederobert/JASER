package fr.lehtto.jaser.dns.entity;

import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * DNS header.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Header(short id, @NotNull Flags flags, short qdcount, short ancount, short nscount, short arcount)
    implements Writable {

  /**
   * The size of the header in bytes.
   */
  public static final int HEADER_SIZE = 12;

  // region Constants
  public static final int ID_LENGTH = 16; // 16 bits
  public static final int QDCOUNT_LENGTH = 16; // 16 bits
  public static final int ANCOUNT_LENGTH = 16; // 16 bits
  public static final int NSCOUNT_LENGTH = 16; // 16 bits
  public static final int ARCOUNT_LENGTH = 16; // 16 bits
  // endregion

  private static final Logger LOG = LogManager.getLogger(Header.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getBytes() {
    final byte[] bytes = new byte[HEADER_SIZE];
    final ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.putShort(id);
    bb.put(flags.getBytes());
    bb.putShort(qdcount);
    bb.putShort(ancount);
    bb.putShort(nscount);
    bb.putShort(arcount);

    return bytes;
  }
}
