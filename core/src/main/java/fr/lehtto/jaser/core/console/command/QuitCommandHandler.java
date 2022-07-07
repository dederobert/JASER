package fr.lehtto.jaser.core.console.command;

import fr.lehtto.jaser.core.console.Cui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command handler for the quit command.
 *
 * @author lehtto
 * @since 0.2.0
 */
public class QuitCommandHandler extends CommandHandler {

  private final Cui cui;

  /**
   * Valued constructor.
   *
   * @param next the next command handler
   * @param cui the cui to use
   */
  public QuitCommandHandler(final @Nullable CommandHandler next, final Cui cui) {
    super(next);
    this.cui = cui;
  }

  @Override
  protected boolean handleCommand(final @NotNull String line) {
    if (!"q".equals(line) && !"quit".equals(line)) {
      return false;
    }
    cui.stop();
    return true;
  }
}
