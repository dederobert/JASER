package fr.lehtto.jaser.core.utils;

/**
 * A side effect of the null handling is that a NullPointerException should be considered a bug in StringUtils.
 * <p>
 * Methods in this class include sample code in their Javadoc comments to explain their operation. The symbol * is used
 * to indicate any input including null.
 *
 * @author Apache software foundation (commons-lang3)
 */
public final class StringUtils {

  /**
   * Default constructor.
   */
  private StringUtils() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Checks if a CharSequence is empty ("") or null.
   * <pre>
   *  {@code StringUtils.isEmpty(null)      = true}
   *  {@code StringUtils.isEmpty("")        = true}
   *  {@code StringUtils.isEmpty(" ")       = false}
   *  {@code StringUtils.isEmpty("bob")     = false}
   *  {@code StringUtils.isEmpty("  bob  ") = false}
   * </pre>
   * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence. That functionality is available
   * in isBlank().
   *
   * @param cs the CharSequence to check, may be null
   * @return true if the CharSequence is empty or null
   */
  @SuppressWarnings("SizeReplaceableByIsEmpty")
  public static boolean isEmpty(final CharSequence cs) {
    return null == cs || 0 == cs.length();
  }

}
