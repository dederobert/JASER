package fr.lehtto.jaser.dns.master.file;

import org.jetbrains.annotations.NotNull;

/**
 * State machine to tokenize a line.
 *
 * @author lehtto
 * @since 0.2.0
 */
enum TokenizeStateMachine {
  START {
    @Override
    public TokenizeStateMachine next(final char c) {
      if (Character.isAlphabetic(c)) {
        return DOMAIN;
      }
      return switch (c) {
        case ';' -> COMMENT;
        case '$' -> CONTROL;
        case ' ', '\t' -> SPACE;
        case '\n' -> NEWLINE;
        default -> INVALID;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.START;
    }
  },
  COMMENT {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '\n' -> NEWLINE;
        default -> COMMENT;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.COMMENT;
    }
  },
  CONTROL {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '\n' -> NEWLINE;
        case '\t', ' ' -> SPACE;
        default -> CONTROL;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.CONTROL;
    }
  },
  DOMAIN {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '\t', ' ' -> SPACE;
        case '\n' -> NEWLINE;
        case ';' -> COMMENT;
        default -> DOMAIN;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.DOMAIN;
    }
  },
  SPACE {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case ' ', '\t' -> SPACE;
        case '\n' -> NEWLINE;
        case ';' -> COMMENT;
        case '"' -> QUOTE;
        default -> DATA;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.SPACE;
    }
  },
  DATA {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '\t', ' ' -> SPACE;
        case '\n' -> NEWLINE;
        case ';' -> COMMENT;
        default -> DATA;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.DATA;
    }
  },
  QUOTE {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '"' -> END_QUOTE;
        default -> QUOTE;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.QUOTE;
    }
  },
  END_QUOTE {
    @Override
    public TokenizeStateMachine next(final char c) {
      return switch (c) {
        case '\t', ' ' -> SPACE;
        case '\n' -> NEWLINE;
        case ';' -> COMMENT;
        default -> INVALID;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.END_QUOTE;
    }
  },
  NEWLINE {
    @Override
    public TokenizeStateMachine next(final char c) {
      if (Character.isAlphabetic(c)) {
        return DOMAIN;
      }

      return switch (c) {
        case ';' -> COMMENT;
        case '$' -> CONTROL;
        case ' ', '\t' -> SPACE;
        case '\n' -> NEWLINE;
        default -> INVALID;
      };
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.NEWLINE;
    }
  },
  INVALID {
    @Override
    public TokenizeStateMachine next(final char c) {
      return INVALID;
    }

    @Override
    public @NotNull TokenType getType() {
      return TokenType.INVALID;
    }
  }
  ;

  /**
   * Gets the next state based on character.
   *
   * @param c the character to check
   * @return the next state
   */
  public abstract TokenizeStateMachine next(final char c);

  /**
   * Gets the type of the token.
   *
   * @return the type of the token
   */
  public abstract @NotNull TokenType getType();
}
