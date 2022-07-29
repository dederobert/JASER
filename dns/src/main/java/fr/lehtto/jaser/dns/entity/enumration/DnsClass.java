package fr.lehtto.jaser.dns.entity.enumration;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * DNS resource record class.
 *
 * @author Lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
public enum DnsClass {
  /**
   * Internet (IN) (1).
   */
  IN(1),
  /**
   * CSNET (2) (Obsolete).
   */
  CS(2),
  /**
   * CHAOS (3).
   */
  CH(3),
  /**
   * Hesiod (4) (Dyer 87).
   */
  HS(4);

  private static final int CLASS_LENGTH = 2;
  private final short value;

  /**
   * Valued constructor.
   *
   * @param value the class value
   */
  DnsClass(final int value) {
    this.value = (short) value;
  }

  /**
   * Gets class from value.
   *
   * @param value the class value
   * @return the class
   */
  public static Optional<DnsClass> fromValue(final short value) {
    for (final DnsClass dnsClass : values()) {
      if (dnsClass.value == value) {
        return Optional.of(dnsClass);
      }
    }
    return Optional.empty();
  }

  /**
   * Gets the class value.
   *
   * @return the class value
   */
  public short getValue() {
    return value;
  }

  /**
   * Gets array of bytes representing the class.
   *
   * @return the array of bytes representing the class
   */
  public byte @NotNull [] getBytes() {
    return new byte[]{(byte) (value >> 8), (byte) (value & 0x00FF)};
  }

  /**
   * Gets the class size in bytes.
   *
   * @return the class size in bytes
   * @since 1.0.0
   */
  public int getLength() {
    return CLASS_LENGTH;
  }
}
