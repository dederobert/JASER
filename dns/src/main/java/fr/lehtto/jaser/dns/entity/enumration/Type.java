package fr.lehtto.jaser.dns.entity.enumration;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * DNS resource record type enumeration.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
public enum Type {
  /**
   * A (IPv4) address.
   */
  A(1),
  /**
   * NS (name server) record.
   */
  NS(2),
  /**
   * MD (mail destination) record.
   */
  MD(3),
  /**
   * MF (mail forwarder) record.
   */
  MF(4),
  /**
   * CNAME (canonical name) record.
   */
  CNAME(5),
  /**
   * SOA (start of authority) record.
   */
  SOA(6),
  /**
   * MB (mailbox) record.
   */
  MB(7),
  /**
   * MG (mail group) record.
   */
  MG(8),
  /**
   * MR (mail rename) record.
   */
  MR(9),
  /**
   * NULL (null) record.
   */
  NULL(10),
  /**
   * WKS (well known service) record.
   */
  WKS(11),
  /**
   * PTR (pointer) record.
   */
  PTR(12),
  /**
   * HINFO (host information) record.
   */
  HINFO(13),
  /**
   * MINFO (mailbox information) record.
   */
  MINFO(14),
  /**
   * MX (mail exchange) record.
   */
  MX(15),
  /**
   * TXT (text) record.
   */
  TXT(16),
  // Unofficial types
  /**
   * RP (responsible person) record.
   */
  RP(17),
  /**
   * AFSDB (AFS database) record.
   */
  AFSDB(18),
  /**
   * X25 (X.25 address) record.
   */
  X25(19),
  // Query types
  /**
   * AXFR (zone transfer) record.
   */
  AXFR(252),
  /**
   * MAILB (mailbox) record.
   */
  MAILB(253),
  /**
   * MAILA (mail agent) record.
   */
  MAILA(254),
  /**
   * ANY (any) record.
   */
  ANY(255);

  /**
   * The type value.
   */
  private final short typeValue;

  /**
   * Valued constructor.
   *
   * @param typeValue the type value
   */
  Type(final int typeValue) {
    this.typeValue = (short) typeValue;
  }

  /**
   * Gets type from value.
   *
   * @param typeValue the type value
   * @return the type
   */
  public static @NotNull Optional<@NotNull Type> fromTypeValue(final short typeValue) {
    for (final Type type : values()) {
      if (type.typeValue == typeValue) {
        return Optional.of(type);
      }
    }
    return Optional.empty();
  }

  /**
   * Gets the type value.
   *
   * @return the type value
   */
  public short getTypeValue() {
    return typeValue;
  }

  /**
   * Gets array of bytes representing the type.
   *
   * @return the array of bytes
   */
  public byte[] getBytes() {
    return new byte[]{(byte) (typeValue >> 8), (byte) (typeValue & 0x00FF)};
  }
}
