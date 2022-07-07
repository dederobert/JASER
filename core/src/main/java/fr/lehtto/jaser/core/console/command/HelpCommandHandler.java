package fr.lehtto.jaser.core.console.command;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command handler for the help command.
 *
 * @author lehtto
 * @since 0.2.0
 */
public class HelpCommandHandler extends CommandHandler {

  private static final Logger LOG = LogManager.getLogger(HelpCommandHandler.class);
  private final List<String> helpLines = new ArrayList<>();

  /**
   * Valued constructor.
   *
   * @param next the next command handler
   */
  public HelpCommandHandler(final @Nullable CommandHandler next) {
    super(next);
  }

  @Override
  protected boolean handleCommand(final @NotNull String line) {
    if (!"h".equals(line) && !"help".equals(line)) {
      return false;
    }

    LOG.info("Commands:");
    for (final String helpLine : helpLines) {
      LOG.info(helpLine);
    }
    LOG.info("  quit, q: quit");
    LOG.info("  help, h: help");
    return true;
  }

  /**
   * Adds a help line.
   *
   * @param helpLine the help line to add
   */
  public void withHelpLine(final @NotNull String helpLine) {
    helpLines.add(helpLine);
  }
}
