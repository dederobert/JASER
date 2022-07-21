package fr.lehtto.jaser.dns.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HexFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * IPV6 address entity.
 *
 * @author lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public record AddressV6(short @NotNull [] address) implements Writable {

  private static final int HEX_RADIX = 16;
  private static final int NUMBER_OF_BYTE = 16;

  /**
   * Creates a new instance of IPV6 address.
   *
   * @param a1 The first byte of the address.
   * @param a2 The second byte of the address.
   * @param a3 The third byte of the address.
   * @param a4 The fourth byte of the address.
   * @param a5 The fifth byte of the address.
   * @param a6 The sixth byte of the address.
   * @param a7 The seventh byte of the address.
   * @param a8 The eighth byte of the address.
   * @return The new instance of IPV6 address.
   */
  @SuppressWarnings("MethodWithTooManyParameters")
  public static AddressV6 of(
      final @Range(from = 0, to = 65_536) int a1,
      final @Range(from = 0, to = 65_536) int a2,
      final @Range(from = 0, to = 65_536) int a3,
      final @Range(from = 0, to = 65_536) int a4,
      final @Range(from = 0, to = 65_536) int a5,
      final @Range(from = 0, to = 65_536) int a6,
      final @Range(from = 0, to = 65_536) int a7,
      final @Range(from = 0, to = 65_536) int a8
  ) {
    return new AddressV6(
        new short[]{(short) a1, (short) a2, (short) a3, (short) a4, (short) a5, (short) a6, (short) a7, (short) a8});
  }

  /**
   * Creates a new instance of IPV6 address.
   *
   * @param address The address to create.
   * @return The new instance of IPV6 address.
   */
  public static AddressV6 of(final @NotNull String address) {
    final String[] parts = address.split(":");
    final int length = parts.length;
    final short[] shorts = new short[length];
    for (int i = 0; i < length; i++) {
      shorts[i] = (short) Integer.parseInt(parts[i], HEX_RADIX);
    }
    return new AddressV6(shorts);
  }

  @Override
  public byte @NotNull [] getBytes() {
    return ByteBuffer.allocate(NUMBER_OF_BYTE)
        .putShort(address[0])
        .putShort(address[1])
        .putShort(address[2])
        .putShort(address[3])
        .putShort(address[4])
        .putShort(address[5])
        .putShort(address[6])
        .putShort(address[7])
        .array();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final AddressV6 addressV4 = (AddressV6) o;
    return Arrays.equals(address, addressV4.address);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(address);
  }

  @Override
  public String toString() {
    return "IPV6{" +
        HexFormat.of().toHexDigits(address[0]) + ':' +
        HexFormat.of().toHexDigits(address[1]) + ':' +
        HexFormat.of().toHexDigits(address[2]) + ':' +
        HexFormat.of().toHexDigits(address[3]) + ':' +
        HexFormat.of().toHexDigits(address[4]) + ':' +
        HexFormat.of().toHexDigits(address[5]) + ':' +
        HexFormat.of().toHexDigits(address[6]) + ':' +
        HexFormat.of().toHexDigits(address[7]) + '}';
  }
}
