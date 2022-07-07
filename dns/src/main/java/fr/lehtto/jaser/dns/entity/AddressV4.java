package fr.lehtto.jaser.dns.entity;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * IPV4 address entity.
 *
 * @author lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public record AddressV4(byte @NotNull [] address) implements Writable {

  /**
   * Creates a new instance of IPV4 address.
   *
   * @param a1 The first byte of the address.
   * @param a2 The second byte of the address.
   * @param a3 The third byte of the address.
   * @param a4 The fourth byte of the address.
   * @return The new instance of IPV4 address.
   */
  public static AddressV4 of(
      final @Range(from = 0, to = 255) int a1,
      final @Range(from = 0, to = 255) int a2,
      final @Range(from = 0, to = 255) int a3,
      final @Range(from = 0, to = 255) int a4) {
    return new AddressV4(new byte[]{(byte) a1, (byte) a2, (byte) a3, (byte) a4});
  }

  /**
   * Creates a new instance of IPV4 address.
   *
   * @param address The address to create.
   * @return The new instance of IPV4 address.
   */
  public static AddressV4 of(final @NotNull String address) {
    final String[] parts = address.split("[.]");
    final int length = parts.length;
    final byte[] bytes = new byte[length];
    for (int i = 0; i < length; i++) {
      bytes[i] = (byte) Integer.parseInt(parts[i]);
    }
    return new AddressV4(bytes);
  }

  @Override
  public byte @NotNull [] getBytes() {
    return Arrays.copyOf(address, 4);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final AddressV4 addressV4 = (AddressV4) o;
    return Arrays.equals(address, addressV4.address);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(address);
  }

  @Override
  public String toString() {
    return "IPV4{" +
        Byte.toUnsignedInt(address[0]) + '.' +
        Byte.toUnsignedInt(address[1]) + '.' +
        Byte.toUnsignedInt(address[2]) + '.' +
        Byte.toUnsignedInt(address[3]) + '}';
  }
}
