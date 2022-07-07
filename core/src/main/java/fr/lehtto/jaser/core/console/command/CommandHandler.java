package fr.lehtto.jaser.core.console.command;

import fr.lehtto.jaser.core.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command handler.
 *
 * @author lehtto
 * @since 0.2.0
 */
public abstract class CommandHandler {

  private static final Logger LOG = LogManager.getLogger(CommandHandler.class);

  private @Nullable CommandHandler next;

  /**
   * Valued constructor.
   *
   * @param next the next command handler
   */
  protected CommandHandler(final @Nullable CommandHandler next) {
    this.next = next;
  }

  /**
   * Handles the command.
   *
   * @param line the line to handle
   */
  public void handle(final @Nullable String line) {
    if (StringUtils.isEmpty(line)) {
      return;
    }

    if (handleCommand(line)) {
      return;
    }

    if (null != next) {
      next.handle(line);
    } else {
      LOG.error("Unknown command: {}", line);
    }
  }

  /**
   * Handles the command.
   *
   * @param line the line to handle
   * @return true if the command was handled, false otherwise
   */
  protected abstract boolean handleCommand(final @NotNull String line);

  /**
   * Gets the next command handler.
   *
   * @return the next command handler
   */
  public @Nullable CommandHandler getNext() {
    return next;
  }

  /**
   * Sets the next command handler.
   *
   * @param next the next command handler
   */
  public void setNext(final @Nullable CommandHandler next) {
    this.next = next;
  }

}
