package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.console.Cui;
import fr.lehtto.jaser.dns.console.RecordsCommandHandler;
import fr.lehtto.jaser.dns.metrics.MetricsService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Start command.
 *
 * @author Lehtto
 * @version 0.2.0
 * @since 0.1.0
 */
@Command(name = "start", mixinStandardHelpOptions = true,
         description = "Starts the DNS server")
public class StartCommand implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(StartCommand.class);
  private @Nullable MetricsService metricsService;

  @SuppressWarnings("MismatchedReadAndWriteOfArray")
  @Option(names = {"-F", "--file"}, paramLabel = "FILE", required = true,
          description = "One or more DNS zone files to load")
  private File[] files;

  @Option(names = {"-M", "--metrics"}, defaultValue = "false",
          description = "Enable metrics server")
  private boolean metricsEnabled;

  @Option(names = "--metrics-address", paramLabel = "IP",
          defaultValue = "localhost", description = "Metrics server address")
  private InetAddress metricsAddress;

  @Option(names = "--metrics-port", paramLabel = "PORT", defaultValue = "8080",
          description = "Metrics server port")
  private int metricsPort;

  @Parameters(index = "0", arity = "0..1", defaultValue = "localhost",
              description = "AddressV4 of the DNS server")
  private InetAddress ip;

  @Parameters(index = "1", arity = "0..1", defaultValue = "53",
              description = "Port to listen on")
  private int port;

  /**
   * Command entry point.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    final int exitCode = new CommandLine(new StartCommand()).execute(args);
    // noinspection CallToSystemExit
    System.exit(exitCode);
  }

  /**
   * Handles the command.
   */
  @Override
  public void run() {
    // Starts the metricsService server
    if (metricsEnabled) {
      metricsService = new MetricsService(metricsAddress, metricsPort);
      metricsService.start();
      Dns.INSTANCE.setMetricsService(metricsService);
    }

    // Create a new DNS server and start it
    for (final File file : files) {
      Dns.INSTANCE.load(file);
    }
    Dns.INSTANCE.start(ip, port);

    // Create a new CUI and start it
    final Cui cui = new Cui();
    cui.withCommonCommandHandler();
    cui.withHelper("  records, r: print records");
    cui.addCommandHandler(RecordsCommandHandler.class);
    cui.start();

    try {
      Dns.INSTANCE.close();
    } catch (final IOException e) {
      LOG.error("Error while closing the DNS server", e);
    }
    if (null != metricsService) {
      metricsService.close();
    }
  }
}
