package fr.lehtto.jaser.core.utils;

/**
 * Utility library to provide helper methods for Java enums.
 *
 * @author Apache Software Foundation (commons-lang3)
 */
public final class EnumUtils {

  /**
   * Default constructor.
   */
  private EnumUtils() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Checks if the specified name is a valid enum for the class.
   * <p>
   * This method differs from Enum.valueOf(java.lang.Class<T>, java.lang.String) in that checks if the name is a valid
   * enum without needing to catch the exception.
   *
   * @param enumClass the enum name, null returns false
   * @param enumName  the class of the enum to query, not null
   * @param <E>       the type of the enumeration
   * @return true if the enum name is valid, otherwise false
   */
  public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
    return null != getEnum(enumClass, enumName);
  }

  /**
   * Gets the enum for the class, returning null if not found.
   * <p>
   * This method differs from Enum.valueOf(java.lang.Class<T>, java.lang.String) in that it does not throw an exception
   * for an invalid enum name.
   *
   * @param enumClass the class of the enum to query, not null
   * @param enumName  the enum name, null returns null
   * @param <E>       the type of the enumeration
   * @return the enum, null if not found
   */
  public static <E extends Enum<E>> Object getEnum(final Class<E> enumClass, final String enumName) {
    return getEnum(enumClass, enumName, null);
  }

  /**
   * Gets the enum for the class, returning defaultEnum if not found.
   * <p>
   * This method differs from Enum.valueOf(java.lang.Class<T>, java.lang.String) in that it does not throw an exception
   * for an invalid enum name.
   *
   * @param enumClass   the class of the enum to query, not null
   * @param enumName    the enum name, null returns default enum
   * @param defaultEnum the default enum
   * @param <E>         the type of the enumeration
   * @return the enum, default enum if not found
   */
  public static <E extends Enum<E>> Object getEnum(final Class<E> enumClass, final String enumName,
      final E defaultEnum) {
    if (null == enumName) {
      return defaultEnum;
    }
    try {
      return Enum.valueOf(enumClass, enumName);
    } catch (final IllegalArgumentException e) {
      return defaultEnum;
    }
  }

}
