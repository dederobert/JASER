package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.UdpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
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

  @Parameters(index = "0", arity = "0..1", defaultValue = "localhost", description = "Address of the DNS server")
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
    Thread serverThread = null;
    // Create a new DNS server
    try (final @NotNull UdpServer<DnsClientHandler> server = new UdpServer<>(port, ip, DnsClientHandler.class)) {
      // Start the server in a new thread
      LOG.debug("Starting server...");
      serverThread = new Thread(server, "server");
      serverThread.start();

      readConsoleInputs();
    } catch (final IOException e) {
      LOG.error("Error while creating server", e);
    }
    waitServerThread(serverThread);
  }

  /**
   * Reads the console inputs.
   */
  private void readConsoleInputs() {
    try (final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
      boolean running = true;
      while (running) {
        final String line = scanner.nextLine();
        if ("q".equals(line)) {
          running = false;
        }
      }
    }
  }


  /**
   * Waits for the server thread to finish.
   *
   * @param thread the thread to wait for
   */
  private void waitServerThread(final @Nullable Thread thread) {
    if (null != thread) {
      LOG.info("Waiting for server thread to finish...");
      try {
        thread.join();
      } catch (final InterruptedException e) {
        LOG.error("Server thread interrupted", e);
        thread.interrupt();
      }
      LOG.info("Server thread finished");
    }
  }
}
