package fr.lehtto.jaser.core.utils;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

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
    logger.log(level, "{} :", prefix);
    for (final Object element : list) {
      logger.log(level, "  - {}", element);
    }
  }
}
