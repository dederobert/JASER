package fr.lehtto.jaser.core.utils;

import fr.lehtto.jaser.core.logging.Level;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * BeautifulListLogger.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public final class BeautifulListLogger {

  /**
   * Default constructor.
   */
  private BeautifulListLogger() {
    throw new AssertionError("This constructor should not be called");
  }

  /**
   * Logs a list of objects.
   *
   * @param logger the logger to use
   * @param level  the level to use
   * @param prefix the prefix to use
   * @param list   the list to log
   */
  public static void log(final @NotNull Logger logger, final @NotNull Level level, final @NotNull String prefix,
      final @NotNull List<?> list) {
    switch (level) {
      case TRACE -> print(logger::trace, prefix, list);
      case DEBUG -> print(logger::debug, prefix, list);
      case INFO -> print(logger::info, prefix, list);
      case WARN -> print(logger::warn, prefix, list);
      case ERROR -> print(logger::error, prefix, list);
    }
  }

  /**
   * Prints a list of objects.
   *
   * @param log    the log method to use
   * @param prefix the prefix to use
   * @param list   the list to log
   */
  private static void print(final LogFunction log, final @NotNull String prefix,
      final @NotNull List<?> list) {
    log.apply("{} :", prefix);
    for (final Object element : list) {
      log.apply("  - {}", element);
    }
  }

  /**
   * Logger function.
   */
  @FunctionalInterface
  interface LogFunction {

    /**
     * Applies the function.
     *
     * @param pattern the prefix to use
     * @param args    the arguments to use
     */
    void apply(String pattern, Object... args);
  }
}
