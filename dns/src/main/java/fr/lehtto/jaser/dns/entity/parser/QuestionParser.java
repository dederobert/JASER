package fr.lehtto.jaser.dns.entity.parser;

import fr.lehtto.jaser.dns.entity.Question;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Question parser.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final class QuestionParser {

  /**
   * Default constructor.
   */
  private QuestionParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Parses a question from a byte array.
   *
   * @param bytes  the bytes to parse
   * @param offset the offset to start parsing from
   * @param length the length of the bytes to parse
   * @return the parsed question
   */
  public static @NotNull Set<Question> parse(final byte @NotNull [] bytes, final int offset, final int length) {
    final StringBuilder name = new StringBuilder(length - offset);
    int i = offset;

    final Set<Question> questions = new HashSet<>();
    while (i < length) {
      while (0 != bytes[i]) {
        name.append(new String(bytes, i, 1, StandardCharsets.UTF_8));
        i++;
      }

      final byte qTypeByte1 = bytes[i + 1];
      final byte qTypeByte2 = bytes[i + 2];
      final byte qClassByte1 = bytes[i + 3];
      final byte qClassByte2 = bytes[i + 4];

      final short qTypeValue = (short) ((qTypeByte1 & 0xFFFF) << 8 | qTypeByte2 & 0x00FF);
      final Type type = Type.fromTypeValue(qTypeValue)
          .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + qTypeValue));

      final short qClassValue = (short) ((qClassByte1 & 0xFFFF) << 8 | qClassByte2 & 0x00FF);
      final DnsClass dnsClass = DnsClass.fromValue(qClassValue)
          .orElseThrow(() -> new IllegalArgumentException("Invalid class value: " + qClassValue));
      questions.add(new Question(name.toString(), type, dnsClass));
      i+=5;
    }
    return questions;
  }

}
