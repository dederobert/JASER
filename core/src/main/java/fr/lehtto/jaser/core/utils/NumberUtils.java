package fr.lehtto.jaser.core.utils;

/**
 * Provides extra functionality for Java Number classes.
 *
 * @author Apache Software Foundation (commons-lang3)
 */
public final class NumberUtils {

  /**
   * Default constructor.
   */
  private NumberUtils() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Checks whether the given String is a parsable number.
   * <p>
   * Parsable numbers include those Strings understood by Integer.parseInt(String), Long.parseLong(String),
   * Float.parseFloat(String) or Double.parseDouble(String). This method can be used instead of catching ParseException
   * when calling one of those methods.
   * <p>
   * Hexadecimal and scientific notations are not considered parsable. See isCreatable(String) on those cases.
   * <p>
   * Null and empty String will return false.
   *
   * @param s the String to check.
   * @return true if the string is a parsable number.
   */
  public static boolean isParsable(final String s) {
    if (StringUtils.isEmpty(s)) {
      return false;
    }

    if ('.' == s.charAt(s.length() - 1)) {
      return false;
    }

    if ('-' == s.charAt(0)) {
      return 1 != s.length() && withDecimalsParsing(s, 1);
    }
    return withDecimalsParsing(s, 0);
  }

  @SuppressWarnings("JavaDoc")
  private static boolean withDecimalsParsing(final String str, final int beginIndex) {
    int decimalPoints = 0;
    for (int i = beginIndex; i < str.length(); i++) {
      final boolean isDecimalPoint = '.' == str.charAt(i);
      if (isDecimalPoint) {
        decimalPoints++;
      }
      if (1 < decimalPoints) {
        return false;
      }
      if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
