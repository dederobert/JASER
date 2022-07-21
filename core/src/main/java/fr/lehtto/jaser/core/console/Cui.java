package fr.lehtto.jaser.core.console;

import fr.lehtto.jaser.core.console.command.CommandHandler;
import fr.lehtto.jaser.core.console.command.HelpCommandHandler;
import fr.lehtto.jaser.core.console.command.QuitCommandHandler;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Console user interface.
 *
 * @author Lehtto
 * @since 0.2.0
 */
public class Cui {

  private static final Logger LOG = LoggerFactory.getLogger(Cui.class);
  private boolean running = true;

  private HelpCommandHandler helpCommandHandler;
  private CommandHandler commandHandler;

  /**
   * Reads the console inputs.
   */
  public void start() {
    LOG.info("Entering console mode");
    LOG.info("Type 'h' or 'help' for help");
    try (final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
      while (running) {
        final String line = scanner.nextLine();
        commandHandler.handle(line);
      }
    }
  }

  /**
   * Adds the common command handlers.
   */
  public void withCommonCommandHandler() {
    helpCommandHandler = new HelpCommandHandler(commandHandler);
    commandHandler = new QuitCommandHandler(helpCommandHandler, this);
  }

  /**
   * Adds a command handler.
   *
   * @param commandHandlerClass the command handler class
   */
  public void addCommandHandler(final Class<? extends CommandHandler> commandHandlerClass) {
    try {
      commandHandler = commandHandlerClass.getDeclaredConstructor(CommandHandler.class)
          .newInstance(this.commandHandler);
    } catch (final InstantiationException e) {
      LOG.error("Error while adding command handler. CommandHandler must not be abstract.", e);
    } catch (final IllegalAccessException e) {
      LOG.error("Error while adding command handler. CommandHandler must not be private.", e);
    } catch (final InvocationTargetException e) {
      LOG.error("Error while adding command handler. CommandHandler must not throw an exception.", e);
    } catch (final NoSuchMethodException e) {
      LOG.error(
          "Error while adding command handler. CommandHandler must have a constructor "
              + "with a CommandHandler as parameter.", e);
    }
  }

  /**
   * Stops the console.
   */
  public void stop() {
    running = false;
  }

  /**
   * Adds a help line.
   *
   * @param helpLine the help line to add
   */
  public void withHelper(final @NotNull String helpLine) {
    if (null != helpCommandHandler) {
      helpCommandHandler.withHelpLine(helpLine);
    }
  }
}
