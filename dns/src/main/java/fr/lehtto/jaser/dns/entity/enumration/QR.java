package fr.lehtto.jaser.dns.entity.enumration;

/**
 * DNS QR.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public enum QR {
  QUERY(0), RESPONSE(1);

  private final int value;

  /**
   * Valued constructor.
   *
   * @param value the value
   */
  QR(final int value) {
    this.value = value;
  }

  /**
   * Gets the value of this QR.
   *
   * @return the value of this QR
   */
  public int getValue() {
    return value;
  }
}
