package fr.lehtto.jaser.dns.entity.parser;

import fr.lehtto.jaser.dns.entity.Flags;
import fr.lehtto.jaser.dns.entity.enumration.OpCode;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for DNS flags.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("MagicNumber")
public final class FlagParser {

  /**
   * Default constructor.
   */
  private FlagParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Parses a DNS flags from a byte array.
   *
   * @param headerBytes the header bytes
   * @return the flags
   */
  @Contract("_ -> new")
  public static @NotNull Flags parse(final byte @NotNull [] headerBytes) {
    return new Flags(getQr(headerBytes), getOpcode(headerBytes), getAa(headerBytes), getTc(headerBytes),
        getRd(headerBytes), getRa(headerBytes), getZ(headerBytes), getRcode(headerBytes));
  }

  /**
   * Gets the QR flag.
   * <p>
   * The QR flag is set to 0 if the message is a query, 1 if it is a response.
   *
   * @param headerBytes the header bytes
   * @return the QR flag
   */
  @Contract(pure = true)
  private static QR getQr(final byte @NotNull [] headerBytes) {
    // Get the 17th bit of the header
    // 1. Get bits 17..24 of the header (the third byte)
    // 2. Mask all bits except the 17th bit (0b1000_0000)
    // 3. If the 17th bit is 1, return true, else return false
    return 0b1000_0000 == (headerBytes[2] & 0b1000_0000) ? QR.RESPONSE : QR.QUERY;
  }

  /**
   * Gets the {@link OpCode OPCODE}.
   *
   * @param headerBytes the header bytes
   * @return the {@link OpCode OPCODE}
   */
  @Contract(pure = true)
  private static OpCode getOpcode(final byte @NotNull [] headerBytes) {
    // Get the 18th to 21st bits of the header
    // 1. Get bits 17..24 of the header (the third byte)
    // 2. Mask all bits except the 18th to 21st bits (0x78)
    // 3. Shift the 18th to 21st bits to the right 3 times (>> 3)
    // 4. Cast the result to an OpCode enum value
    return OpCode.values()[(headerBytes[2] & 0b0111_1000) >> 3];
  }

  /**
   * Gets the AA flag.
   * <p>
   * The AA flag is set to 1 if the server is an authority for the domain name in the question section, 0 otherwise.
   *
   * @param headerBytes the header bytes
   * @return the AA flag
   */
  @Contract(pure = true)
  private static boolean getAa(final byte @NotNull [] headerBytes) {
    // Get the 22nd bit of the header
    // 1. Get bits 17..24 of the header (the third byte)
    // 2. Mask all bits except the 22nd bit (0x40)
    // 3. If the 22nd bit is 1, return true, else return false
    return 0b0000_0100 == (headerBytes[2] & 0b0000_0100);
  }

  /**
   * Gets the TC flag.
   * <p>
   * The TC flag is set to 1 if the message was truncated due to length greater than that permitted on the transmission
   * channel.
   *
   * @param headerBytes the header bytes
   * @return the TC flag
   */
  @Contract(pure = true)
  private static boolean getTc(final byte @NotNull [] headerBytes) {
    // Get the 23rd bit of the header
    // 1. Get bits 17..24 of the header (the third byte)
    // 2. Mask all bits except the 23rd bit (0x02)
    // 3. If the 23rd bit is 1, return true, else return false
    return 0b0000_0010 == (headerBytes[2] & 0b0000_0010);
  }

  /**
   * Gets the RD flag.
   * <p>
   * The RD flag is set to 1 if the message is intended for a recursive server.
   *
   * @param headerBytes the header bytes
   * @return the RD flag
   */
  @Contract(pure = true)
  private static boolean getRd(final byte @NotNull [] headerBytes) {
    // Get the 24th bit of the header
    // 1. Get bits 17..24 of the header (the third byte)
    // 2. Mask all bits except the 24th bit (0x01)
    // 3. If the 24th bit is 1, return true, else return false
    return 0b0000_0001 == (headerBytes[2] & 0b0000_0001);
  }

  /**
   * Gets the RA flag.
   * <p>
   * The RA flag is set to 1 if the recursive is available on the server, 0 otherwise.
   *
   * @param headerBytes the header bytes
   * @return the RA flag
   */
  @Contract(pure = true)
  private static boolean getRa(final byte @NotNull [] headerBytes) {
    // Get the 25th bit of the header
    // 1. Get bits 25..32 of the header (the fourth byte)
    // 2. Mask all bits except the 25th bit (0x80)
    // 3. If the 25th bit is 1, return true, else return false
    return 0b1000_0000 == (headerBytes[3] & 0b1000_0000);
  }

  /**
   * Gets the Z flag.
   * <p>
   * The Z flag is composed of 3 bits.
   * <ol>
   *  <li>The Z flag <b>must</b> be zero for all queries.</li>
   *  <li>The AA flag <b>must</b> be set to 1 for all answerRecords.</li>
   *  <li>The NAD flag <b>must</b> be set to 0 for all answerRecords.</li>
   * </ol>
   *
   * @param headerBytes the header bytes
   * @return the Z flag
   */
  @Contract(pure = true)
  private static byte getZ(final byte @NotNull [] headerBytes) {
    // Get the 26th to 28th bits of the header
    // 1. Get bits 25..32 of the header (the fourth byte)
    // 2. Mask all bits except the 26th to 28th bits (0x70)
    // 3. Shift the 26th to 28th bits to the right 4 times (>> 4)
    return (byte) ((headerBytes[3] & 0b0111_0000) >> 4);
  }

  /**
   * Gets the {@link RCode RCODE}.
   *
   * @param headerBytes the header bytes
   * @return the {@link RCode RCODE}
   */
  @Contract(pure = true)
  private static RCode getRcode(final byte @NotNull [] headerBytes) {
    // Get the 29th to 32nd bits of the header
    // 1. Get bits 25..32 of the header (the fourth byte)
    // 2. Mask all bits except the 29th to 32nd bits (0x0F)
    // 3. Cast the result to an RCode enum value
    return RCode.values()[headerBytes[3] & 0b0000_1111];
  }


}
