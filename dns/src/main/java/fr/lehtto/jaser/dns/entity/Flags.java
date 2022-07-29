package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.OpCode;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import org.jetbrains.annotations.NotNull;

/**
 * DNS flags.
 *
 * @author Lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
public record Flags(@NotNull QR qr, @NotNull OpCode opcode, boolean aa, boolean tc, boolean rd, boolean ra, byte z,
                    @NotNull RCode rcode) implements Writable {

  // region Constants
  private static final int FLAGS_LENGTH = 2; // 2 bytes

  public static final int QR_LENGTH = 1; // 1 bit
  public static final int OPCODE_LENGTH = 4; // 4 bits
  public static final int AA_LENGTH = 1; // 1 bit
  public static final int TC_LENGTH = 1; // 1 bit
  public static final int RD_LENGTH = 1; // 1 bit
  public static final int RA_LENGTH = 1; // 1 bit
  public static final int Z_LENGTH = 3; // 3 bits
  public static final int RCODE_LENGTH = 4; // 4 bits

  // endregion

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
    return new byte[]{
        (byte) (qr.getValue() << 7 | opcode.getValue() << 3 | (aa ? 0x4 : 0) | (tc ? 0x2 : 0) | (rd ? 0x1 : 0)),
        (byte) ((ra ? 0x80 : 0) | z << 4 | rcode.getValue())
    };
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return FLAGS_LENGTH;
  }

  /**
   * Builder for {@link Flags}.
   */
  public static class Builder {

    private QR qr;
    private OpCode opcode;
    private boolean aa;
    private boolean tc;
    private boolean rd;
    private boolean ra;
    private byte z;
    private RCode rcode;

    /**
     * Default constructor.
     */
    Builder() {
      qr = null;
      opcode = null;
      aa = false;
      tc = false;
      rd = false;
      ra = false;
      z = -1;
      rcode = null;
    }

    /**
     * Valued constructor.
     *
     * @param flags the flags to set
     */
    Builder(final @NotNull Flags flags) {
      qr = flags.qr;
      opcode = flags.opcode;
      aa = flags.aa;
      tc = flags.tc;
      rd = flags.rd;
      ra = flags.ra;
      z = flags.z;
      rcode = flags.rcode;
    }

    /**
     * Set the QR (query/response) bit.
     *
     * @param qr the QR bit
     * @return the builder
     */
    public Builder qr(final @NotNull QR qr) {
      this.qr = qr;
      return this;
    }

    /**
     * Set the OPCODE (operation code) bits.
     *
     * @param opcode the OPCODE bits
     * @return the builder
     */
    public Builder opcode(final @NotNull OpCode opcode) {
      this.opcode = opcode;
      return this;
    }

    /**
     * Set the AA (authoritative answer) bit.
     *
     * @param aa the AA bit
     * @return the builder
     */
    public Builder aa(final boolean aa) {
      this.aa = aa;
      return this;
    }

    /**
     * Set the TC (truncated) bit.
     *
     * @param tc the TC bit
     * @return the builder
     */
    public Builder tc(final boolean tc) {
      this.tc = tc;
      return this;
    }

    /**
     * Set the RD (recursion desired) bit.
     *
     * @param rd the RD bit
     * @return the builder
     */
    public Builder rd(final boolean rd) {
      this.rd = rd;
      return this;
    }

    /**
     * Set the RA (recursion available) bit.
     *
     * @param ra the RA bit
     * @return the builder
     */
    public Builder ra(final boolean ra) {
      this.ra = ra;
      return this;
    }

    /**
     * Set the Z (reserved) bits.
     *
     * @param z the Z bits
     * @return the builder
     */
    public Builder z(final byte z) {
      this.z = z;
      return this;
    }

    /**
     * Set the RCODE (response code) bits.
     *
     * @param rcode the RCODE bits
     * @return the builder
     */
    public Builder rcode(final @NotNull RCode rcode) {
      this.rcode = rcode;
      return this;
    }

    /**
     * Build the {@link Flags} object.
     *
     * @return the {@link Flags} object
     */
    public Flags build() {
      if (null == qr) {
        throw new IllegalArgumentException("QR bit is not set");
      }
      if (null == opcode) {
        throw new IllegalArgumentException("OPCODE bits are not set");
      }
      if (-1 == z) {
        throw new IllegalArgumentException("Z bits are not set");
      }
      if (null == rcode) {
        throw new IllegalArgumentException("RCODE bits are not set");
      }
      return new Flags(qr, opcode, aa, tc, rd, ra, z, rcode);
    }
  }
}
