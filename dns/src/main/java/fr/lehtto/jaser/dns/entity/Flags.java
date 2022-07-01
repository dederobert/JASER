package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.OpCode;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import org.jetbrains.annotations.NotNull;

/**
 * DNS flags.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
public record Flags(@NotNull QR qr, @NotNull OpCode opcode, boolean aa, boolean tc, boolean rd, boolean ra, byte z,
                    @NotNull RCode rcode) implements Writable {

  // region Constants
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
   * {@inheritDoc}
   */
  @Override
  public byte[] getBytes() {
    return new byte[]{
        (byte) (qr.getValue() << 7 | opcode.getValue() << 3 | (aa ? 0x4 : 0) | (tc ? 0x2 : 0) | (rd ? 0x1 : 0)),
        (byte) ((ra ? 0x80 : 0) | z << 4 | rcode.getValue())
    };
  }
}
