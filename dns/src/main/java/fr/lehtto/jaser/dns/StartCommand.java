package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.utils.BeautifulListLogger;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Start command.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@Command(name = "start", mixinStandardHelpOptions = true, description = "Starts the DNS server")
public class StartCommand implements Runnable {

  private static final Logger LOG = LogManager.getLogger(StartCommand.class);

  @Option(
      names = {"F", "file"},
      defaultValue = "dnsZone.txt",
      description = "File containing the DNS zone")
  private File file;

  @Parameters(index = "0", arity = "0..1", defaultValue = "localhost", description = "AddressV4 of the DNS server")
  private InetAddress ip;

  @Parameters(index = "1", arity = "0..1", defaultValue = "53", description = "Port to listen on")
  private int port;

  /**
   * Command entry point.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    final int exitCode = new CommandLine(new StartCommand()).execute(args);
    //noinspection CallToSystemExit
    System.exit(exitCode);
  }

  /**
   * Handles the command.
   */
  @Override
  public void run() {
    // Create a new DNS server
    try {
      Dns.INSTANCE.load(file);
    } catch (final IOException ex) {
      LOG.error("Error while loading the DNS zone", ex);
      return;
    }

    Dns.INSTANCE.start(ip, port);
    readConsoleInputs();

    try {
      Dns.INSTANCE.close();
    } catch (final IOException e) {
      LOG.error("Error while closing the DNS server", e);
    }
  }

  /**
   * Reads the console inputs.
   */
  private void readConsoleInputs() {
    LOG.info("Entering console mode");
    LOG.info("Type 'h' for help");
    try (final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
      boolean running = true;
      while (running) {
        final String line = scanner.nextLine();
        if ("q".equals(line)) {
          running = false;
        } else if ("r".equals(line)) {
          BeautifulListLogger.log(LOG, Level.INFO, "Resource records", Dns.INSTANCE.getMasterFile());
        } else if ("h".equals(line)) {
          printHelp();
        } else {
          LOG.info("Unknown command: {}", line);
        }
      }
    }
  }

  /**
   * Prints the help.
   */
  private void printHelp() {
    LOG.info("Commands:");
    LOG.info("  q: quit");
    LOG.info("  r: print records");
    LOG.info("  h: help");
  }
}
