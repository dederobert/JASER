package fr.lehtto.jaser.dns.entity.parser;

import fr.lehtto.jaser.dns.entity.Flags;
import fr.lehtto.jaser.dns.entity.Header;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for {@link Header DNS header}.
 */
@SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
public final class HeaderParser {

  /**
   * Default constructor.
   */
  private HeaderParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Parses a DNS header from a byte array.
   *
   * @param headerBytes the header bytes
   * @return the header
   */
  @Contract("_ -> new")
  public static @NotNull Header parse(final byte[] headerBytes) {
    final Flags dnsFlags = FlagParser.parse(headerBytes);
    return new Header(getId(headerBytes), dnsFlags, getQdcount(headerBytes), getAncount(headerBytes),
        getNscount(headerBytes), getArcount(headerBytes));
  }

  /**
   * Gets the ID.
   *
   * @param headerBytes the header bytes
   * @return the ID
   */
  @Contract(pure = true)
  @SuppressWarnings("NumericCastThatLosesPrecision")
  private static short getId(final byte @NotNull [] headerBytes) {
    // Get the first 16 bits of the header
    // 1. Get the first 8 bits of the header (the first byte)
    // 2. Shift the first 8 bits to the left 8 times (<< 8)
    // 3. Get the second 8 bits of the header (the second byte)
    return (short) ((headerBytes[0] & 0xFFFF) << 8 | headerBytes[1] & 0x00FF);
  }

  /**
   * Gets the QDCOUNT.
   * <p>
   * The QDCOUNT field contains the number of entries in the question section.
   *
   * @param headerBytes the header bytes
   * @return the QDCOUNT
   */
  @Contract(pure = true)
  private static short getQdcount(final byte @NotNull [] headerBytes) {
    // Get the 33rd to 48th bits of the header
    // 1. Get bits 33..40 of the header (the fifth byte)
    // 2. Shift the 33rd to 40th bits to the left 8 times (<< 8)
    // 3. Get bits 41..48 of the header (the sixth byte)
    return (short) ((headerBytes[4] & 0xFFFF) << 8 | headerBytes[5] & 0xFFFF);
  }

  /**
   * Gets the NSCOUNT.
   * <p>
   * The NSCOUNT field contains the number of name server resource records in the authority records section.
   *
   * @param headerBytes the header bytes
   * @return the NSCOUNT
   */
  @Contract(pure = true)
  private static short getNscount(final byte @NotNull [] headerBytes) {
    // Get the 65th to 80th bits of the header
    // 1. Get bits 65..72 of the header (the ninth byte)
    // 2. Shift the 65th to 72nd bits to the left 8 times (<< 8)
    // 3. Get bits 73..80 of the header (the tenth byte)
    return (short) ((headerBytes[8] & 0xFFFF) << 8 | headerBytes[9] & 0xFFFF);
  }

  /**
   * Gets the ARCOUNT.
   * <p>
   * The ARCOUNT field contains the number of resource records in the additional records section.
   *
   * @param headerBytes the header bytes
   * @return the ARCOUNT
   */
  @Contract(pure = true)
  private static short getArcount(final byte @NotNull [] headerBytes) {
    // Get the 81st to 96th bits of the header
    // 1. Get bits 81..88 of the header (the eleventh byte)
    // 2. Shift the 81st to 88th bits to the left 8 times (<< 8)
    // 3. Get bits 89..96 of the header (the twelfth byte)
    return (short) ((headerBytes[10] & 0xFFFF) << 8 | headerBytes[11] & 0xFFFF);
  }

  /**
   * Gets the ANCOUNT.
   * <p>
   * The ANCOUNT field contains the number of resource records in the answer section.
   *
   * @param headerBytes the header bytes
   * @return the ANCOUNT
   */
  @Contract(pure = true)
  private static short getAncount(final byte @NotNull [] headerBytes) {
    // Get the 49th to 64th bits of the header
    // 1. Get bits 49..56 of the header (the seventh byte)
    // 2. Shift the 49th to 56th bits to the left 8 times (<< 8)
    // 3. Get bits 57..64 of the header (the eighth byte)
    return (short) ((headerBytes[6] & 0xFFFF) << 8 | headerBytes[7] & 0xFFFF);
  }
}
