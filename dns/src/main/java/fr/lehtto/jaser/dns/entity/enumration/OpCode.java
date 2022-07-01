package fr.lehtto.jaser.dns.entity.enumration;

/**
 * DNS OPCODE enumeration.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public enum OpCode {

  /**
   * Standard query (QUERY) (0).
   */
  QUERY(0b0000),
  /**
   * Inverse query (IQUERY) (1).
   */
  INVERSE_QUERY(0b0001),
  /**
   * TcpServer status request (STATUS) (2).
   */
  STATUS_QUERY(0b0010);

  private final byte value;

  /**
   * Valued constructor.
   *
   * @param value the opcode value
   */
  OpCode(final int value) {
    this.value = (byte) value;
  }

  /**
   * Gets the opcode value.
   *
   * @return the opcode value
   */
  public byte getValue() {
    return value;
  }

}
