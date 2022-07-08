package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.core.utils.EnumUtils;
import fr.lehtto.jaser.core.utils.NumberUtils;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;

/**
 * State machine for parsing a {@link Token}.
 *
 * @author lehtto
 * @since 0.2.0
 */
enum ParseTokenStateMachine {
  START {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      if (TokenType.DATA == token.type()) {
        if (NumberUtils.isParsable(token.value())) {
          return TTL;
        }
        if (EnumUtils.isValidEnum(Type.class, token.value())) {
          return TYPE;
        }
        if (EnumUtils.isValidEnum(DnsClass.class, token.value())) {
          return CLASS;
        }
        return INVALID;
      }
      return switch (token.type()) {
        case DOMAIN -> DOMAIN;
        case COMMENT -> COMMENT;
        case CONTROL -> CONTROL;
        default -> INVALID;
      };
    }
  },
  DOMAIN {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      if (TokenType.DATA == token.type()) {
        if (NumberUtils.isParsable(token.value())) {
          return TTL;
        }
        if (EnumUtils.isValidEnum(Type.class, token.value())) {
          return TYPE;
        }
        if (EnumUtils.isValidEnum(DnsClass.class, token.value())) {
          return CLASS;
        }
      }
      return INVALID;
    }
  },
  TTL {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      if (TokenType.DATA == token.type()) {
        if (EnumUtils.isValidEnum(Type.class, token.value())) {
          return TYPE;
        }
        if (EnumUtils.isValidEnum(DnsClass.class, token.value())) {
          return CLASS;
        }
      }
      return INVALID;
    }
  },
  TYPE {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      if (TokenType.DATA == token.type() || TokenType.QUOTE == token.type()) {
        return DATA;
      }
      return INVALID;
    }
  },
  CLASS {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      if (TokenType.DATA == token.type()) {
        if (NumberUtils.isParsable(token.value())) {
          return TTL;
        }
        if (EnumUtils.isValidEnum(Type.class, token.value())) {
          return TYPE;
        }
      }
      return INVALID;
    }
  },
  DATA {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      return switch (token.type()) {
        case DATA, QUOTE, END_QUOTE -> DATA;
        case COMMENT -> COMMENT;
        default -> INVALID;
      };
    }
  },
  COMMENT {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      return null;
    }
  },
  CONTROL {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      return DATA;
    }
  },
  INVALID {
    @Override
    public ParseTokenStateMachine next(final Token token) {
      return INVALID;
    }
  };

  /**
   * Gets the next state based on token type.
   *
   * @param token the token to get the next state from.
   * @return the next state
   */
  public abstract ParseTokenStateMachine next(final Token token);
}
