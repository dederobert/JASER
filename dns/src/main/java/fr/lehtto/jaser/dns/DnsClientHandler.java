package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.AbstractUdpClientHandler;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.writer.ResponseWriter;
import fr.lehtto.jaser.dns.metrics.Metrics;
import fr.lehtto.jaser.dns.metrics.MetricsService;
import fr.lehtto.jaser.dns.query.handler.QueryHandlerFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for a client connection.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public class DnsClientHandler extends AbstractUdpClientHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DnsClientHandler.class);

  /**
   * Valued constructor.
   *
   * @param packet the client packet
   * @param socket the server socket
   */
  public DnsClientHandler(final @NotNull DatagramPacket packet, @NotNull final DatagramSocket socket) {
    super(packet, socket);
  }

  /**
   * Handles the client connection.
   */
  @Override
  public void run() {
    LOG.info("New client connected from {}", getPacket().getAddress());
    try {
      LOG.trace("Waiting for a query...");
      final Query query = Query.read(getPacket().getData(), getPacket().getLength());
      LOG.debug("Query received: {}", query);
      handleQueryWithMetrics(() -> {
        try {
          final byte[] buffer = new byte[512];
          final int length = ResponseWriter.write(QueryHandlerFactory.fromQuery(query).handle(query), buffer);
          getSocket().send(new DatagramPacket(buffer, length, getPacket().getAddress(), getPacket().getPort()));
        } catch (final IOException e) {
          LOG.error("Error while sending response", e);
        }
      });
    } catch (final Exception e) {
      if (isRunning()) {
        LOG.error("Error while handling client", e);
      }
    }
    LOG.info("Client disconnected from {}", getPacket().getAddress());
  }

  @Override
  protected void stop() {
    // Do nothing
  }

  /**
   * Handles the query with metrics if metrics are enabled. Else, just handles the query.
   *
   * @param runnable the runnable to run
   */
  private void handleQueryWithMetrics(final Runnable runnable) {
    Dns.INSTANCE.getMetricsService().map(MetricsService::getMetrics)
        .map(Metrics::getQueryTimer)
        .ifPresentOrElse(timer -> timer.record(runnable), runnable);
  }
}

