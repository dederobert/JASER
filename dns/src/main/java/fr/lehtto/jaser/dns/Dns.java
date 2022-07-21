package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.UdpServer;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.master.file.MasterFile;
import fr.lehtto.jaser.dns.master.file.MasterFileParser;
import fr.lehtto.jaser.dns.metrics.MetricsService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DNS main class.
 *
 * @author Lehtto
 * @version 0.2.0
 * @since 0.1.0
 */
public final class Dns implements AutoCloseable {

  public static final Dns INSTANCE = new Dns();

  private static final Logger LOG = LoggerFactory.getLogger(Dns.class);
  private final List<MasterFile> masterFiles = new ArrayList<>();
  private Thread serverThread;
  private UdpServer<DnsClientHandler> server;
  private @Nullable MetricsService metricsService;

  /**
   * Valued constructor.
   */
  private Dns() {}

  /**
   * Starts the DNS server.
   *
   * @param ip   address to listen on
   * @param port port to listen on
   */
  void start(final @NotNull InetAddress ip, final int port) {
    server = new UdpServer<>(port, ip, DnsClientHandler.class);
    serverThread = new Thread(server, "server");
    serverThread.start();
  }

  /**
   * Gets the master file.
   *
   * @return the master file
   */
  public List<MasterFile> getMasterFiles() { return masterFiles; }

  /**
   * Loads the DNS zone from a file.
   *
   * @param file the master file to load
   */
  void load(final @NotNull File file) {
    LOG.info("Loading dns zone from {}", file);
    try {
      final MasterFile masterFile = MasterFileParser.parse(file);
      LOG.info("Loaded {} records", masterFile.size());
      masterFiles.add(masterFile);
      getMetricsService()
          .map(MetricsService::getMetrics)
          .ifPresent(metrics -> metrics.incrementZoneSize(masterFile.size()));
    } catch (final InvalidDnsZoneEntryException e) {
      LOG.error("Invalid DNS zone entry: {}", e.getMessage());
    }
  }

  @Override
  public void close() throws IOException {
    server.close();
    try {
      serverThread.join();
    } catch (final InterruptedException e) {
      LOG.error("Error while waiting for server thread to terminate", e);
      serverThread.interrupt();
      throw new IOException(
          "Error while waiting for server thread to terminate", e);
    }
  }

  /**
   * Sets master files.
   *
   * @param masterFiles the master files to set
   */
  @VisibleForTesting
  void initializeMasterFiles(final List<MasterFile> masterFiles) {
    this.masterFiles.addAll(masterFiles);
  }

  /**
   * Gets the metrics service.
   *
   * @return the metrics service
   */
  public @NotNull Optional<MetricsService> getMetricsService() {
    return Optional.ofNullable(metricsService);
  }

  /**
   * Sets the metrics service.
   *
   * @param metricsService the metrics service to set
   */
  void setMetricsService(final @Nullable MetricsService metricsService) {
    this.metricsService = metricsService;
  }
}
