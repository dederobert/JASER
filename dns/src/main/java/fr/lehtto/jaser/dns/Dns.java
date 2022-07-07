package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.UdpServer;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.parser.ResourceRecordParser;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * DNS main class.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public final class Dns implements AutoCloseable {

  public static final Dns INSTANCE = new Dns();

  private static final Logger LOG = LogManager.getLogger(Dns.class);

  private Thread serverThread;
  private UdpServer<DnsClientHandler> server;
  private final List<ResourceRecord> masterFile = new ArrayList<>();

  /**
   * Valued constructor.
   */
  private Dns(){}

  /**
   * Starts the DNS server.
   *
   * @param ip  address to listen on
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
  public List<ResourceRecord> getMasterFile() {
    return List.copyOf(masterFile);
  }

  /**
   * Loads the DNS zone from a file.
   *
   * @param file the master file to load
   * @throws IOException if the file cannot be loaded
   */
  void load(final @NotNull File file) throws IOException {
    LOG.info("Loading dns zone from {}", file);
    try (final Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
      int i = 1;
      while (scanner.hasNextLine()) {
        final String line = scanner.nextLine();
        try {
          masterFile.add(ResourceRecordParser.parse(line));
        } catch (final InvalidDnsZoneEntryException e) {
          LOG.warn("Invalid dns zone entry at line {}, {}. Line is ignored", i, e.getLocalizedMessage());
        }
        i++;
      }
    }
    LOG.info("Loaded {} records", masterFile.size());
  }

  @Override
  public void close() throws IOException {
    server.close();
    try {
      serverThread.join();
    } catch (final InterruptedException e) {
      LOG.error("Error while waiting for server thread to terminate", e);
      serverThread.interrupt();
      throw new IOException("Error while waiting for server thread to terminate", e);
    }
  }
}
