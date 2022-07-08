package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Parse a line of a DNS zone file.
 *
 * @author lehtto
 * @since 0.2.0
 */
final class LineParser {

  /**
   * Default constructor.
   */
  private LineParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Generates tokens from a line of a DNS zone file.
   *
   * @param line the line to parse.
   * @return the list of tokens.
   * @throws InvalidDnsZoneEntryException if the line is invalid.
   */
  static List<Token> tokenize(final @NotNull String line) throws InvalidDnsZoneEntryException {
    TokenizeStateMachine state = TokenizeStateMachine.START;
    TokenizeStateMachine previousState = state;
    final List<Token> tokens = new ArrayList<>();
    StringBuilder sb = new StringBuilder(10);

    for (final char c : line.toCharArray()) {
      state = state.next(c);
      if (TokenizeStateMachine.INVALID == state) {
        throw new InvalidDnsZoneEntryException("Invalid character: " + c);
      }
      if (previousState != state) {
        // Add state if it's not the same as the last one
        tokens.add(new Token(sb.toString(), previousState.getType()));
        sb = new StringBuilder(10);
        previousState = state;
      }
      sb.append(c);
    }
    // Add last token
    tokens.add(new Token(sb.toString(), state.getType()));
    return tokens;
  }

  /**
   * Parse tokens into a DNS zone entry.
   *
   * @param tokens the tokens to parse.
   * @return the DNS zone entry.
   */
  static List<ParsedToken> parseToken(final @NotNull List<Token> tokens) {
    final int tokenListSize = tokens.size();
    final List<ParsedToken> states = new ArrayList<>(tokenListSize);
    ParseTokenStateMachine state = ParseTokenStateMachine.START;

    for (int i = 1; i < tokenListSize; i++) {
      final Token token = tokens.get(i);
      if (TokenType.SPACE != token.type()) {
        state = state.next(token);
        states.add(new ParsedToken(token.value(), state));
      }
    }
    return states;
  }
}
