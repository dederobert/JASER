package fr.lehtto.jaser.dns.entity.enumration;

/**
 * DNS RCODE enumeration.
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public enum RCode {
  /**
   * No error (0).
   */
  NO_ERROR(0b0000),
  /**
   * Format error (1).
   */
  FORMAT_ERROR(0b0001),
  /**
   * TcpServer failure (2).
   */
  SERVER_FAILURE(0b0010),
  /**
   * Name error (3).
   */
  NAME_ERROR(0b0011),
  /**
   * Not implemented (4).
   */
  NOT_IMPLEMENTED(0b0100),
  /**
   * Refused (5).
   */
  REFUSED(0b0101);

  private final byte value;

  /**
   * Valued constructor.
   *
   * @param value the rcode value
   */
  RCode(final int value) {
    this.value = (byte) value;
  }

  /**
   * Gets the rcode value.
   *
   * @return the rcode value
   */
  public byte getValue() {
    return value;
  }
}
