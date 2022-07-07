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
  static final int HEADER_SIZE = 12;

  // region Constants
  public static final int ID_LENGTH = 16; // 16 bits
  public static final int QDCOUNT_LENGTH = 16; // 16 bits
  public static final int ANCOUNT_LENGTH = 16; // 16 bits
  public static final int NSCOUNT_LENGTH = 16; // 16 bits
  public static final int ARCOUNT_LENGTH = 16; // 16 bits
  // endregion

  private static final Logger LOG = LogManager.getLogger(Header.class);

  /**
   * Creates a new builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a new builder.
   *
   * @return the builder
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

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

  /**
   * Builder for {@link Header}.
   */
  public static class Builder {

    private short id;
    private Flags flags;
    private short qdcount;
    private short ancount;
    private short nscount;
    private short arcount;

    /**
     * Default constructor.
     */
    Builder() {
      id = -1;
      flags = null;
      qdcount = -1;
      ancount = -1;
      nscount = -1;
      arcount = -1;
    }

    /**
     * Valued constructor.
     *
     * @param header the header to set
     */
    Builder(final @NotNull Header header) {
      this.id = header.id;
      this.flags = header.flags;
      this.qdcount = header.qdcount;
      this.ancount = header.ancount;
      this.nscount = header.nscount;
      this.arcount = header.arcount;
    }

    /**
     * Sets the id of the header.
     *
     * @param id the id to set
     * @return this builder
     */
    public Builder id(final short id) {
      this.id = id;
      return this;
    }

    /**
     * Sets the flags of the header.
     *
     * @param flags the flags to set
     * @return this builder
     */
    public Builder flags(final @NotNull Flags flags) {
      this.flags = flags;
      return this;
    }

    /**
     * Sets the qdcount (questions count) of the header.
     *
     * @param qdcount the qdcount to set
     * @return this builder
     */
    public Builder qdcount(final short qdcount) {
      this.qdcount = qdcount;
      return this;
    }

    /**
     * Sets the ancount (answer records) of the header.
     *
     * @param ancount the ancount to set
     * @return this builder
     */
    public Builder ancount(final short ancount) {
      this.ancount = ancount;
      return this;
    }

    /**
     * Sets the nscount (authority records) of the header.
     *
     * @param nscount the nscount to set
     * @return this builder
     */
    public Builder nscount(final short nscount) {
      this.nscount = nscount;
      return this;
    }

    /**
     * Sets the arcount (additional records) of the header.
     *
     * @param arcount the arcount to set
     * @return this builder
     */
    public Builder arcount(final short arcount) {
      this.arcount = arcount;
      return this;
    }

    /**
     * Builds the header.
     *
     * @return the header
     */
    public Header build() {
      if (-1 == id) {
        throw new IllegalStateException("id is not set");
      }
      if (null == flags) {
        throw new IllegalStateException("Flags are null");
      }
      if (-1 == qdcount) {
        throw new IllegalStateException("qdcount is not set");
      }
      if (-1 == ancount) {
        throw new IllegalStateException("ancount is not set");
      }
      if (-1 == nscount) {
        throw new IllegalStateException("nscount is not set");
      }
      if (-1 == arcount) {
        throw new IllegalStateException("arcount is not set");
      }
      return new Header(id, flags, qdcount, ancount, nscount, arcount);
    }
  }
}
