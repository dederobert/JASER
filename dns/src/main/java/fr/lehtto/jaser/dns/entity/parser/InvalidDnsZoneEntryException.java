package fr.lehtto.jaser.dns.entity.parser;

import java.io.Serial;

/**
 * Exception thrown when a DNS resource record cannot be parsed.
 */
public class InvalidDnsZoneEntryException extends Exception {

  @Serial
  private static final long serialVersionUID = -6743809534443093917L;

  /**
   * Valued constructor.
   *
   * @param message the message
   * @param args    the arguments
   */
  public InvalidDnsZoneEntryException(final String message, final Object... args) {
    super(String.format(message, args));
  }

  /**
   * Valued constructor.
   *
   * @param cause   the cause
   * @param message the message
   * @param args    the arguments
   */
  public InvalidDnsZoneEntryException(final Throwable cause, final String message, final Object... args) {
    super(String.format(message, args), cause);
  }

}
