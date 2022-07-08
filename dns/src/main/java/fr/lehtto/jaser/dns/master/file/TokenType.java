package fr.lehtto.jaser.dns.master.file;

/**
 * Type of token.
 *
 * @author lehtto
 * @since 0.2.0
 */
enum TokenType {
  DOMAIN,
  COMMENT,
  CONTROL,
  DATA,
  QUOTE,
  END_QUOTE,
  NEWLINE,
  SPACE,
  START,
  INVALID
}
