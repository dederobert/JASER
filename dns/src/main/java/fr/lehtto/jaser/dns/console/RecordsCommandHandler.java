package fr.lehtto.jaser.dns.console;

import fr.lehtto.jaser.core.console.command.CommandHandler;
import fr.lehtto.jaser.core.utils.BeautifulListLogger;
import fr.lehtto.jaser.dns.Dns;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command handler for the records command.
 *
 * @author lehtto
 * @version 0.2.0
 */
public class RecordsCommandHandler extends CommandHandler {

  private static final Logger LOG = LogManager.getLogger(RecordsCommandHandler.class);

  /**
   * Valued constructor.
   *
   * @param next the next command handler
   */
  public RecordsCommandHandler(final @Nullable CommandHandler next) {
    super(next);
  }

  @Override
  protected boolean handleCommand(final @NotNull String line) {
    if (!"r".equals(line) && !"records".equals(line)) {
      return false;
    }

    BeautifulListLogger.log(LOG, Level.INFO, "Resource records", Dns.INSTANCE.getMasterFile());
    return true;
  }
}
