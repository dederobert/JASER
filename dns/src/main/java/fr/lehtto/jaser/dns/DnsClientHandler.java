package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.AbstractUdpClientHandler;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.writer.ResponseWriter;
import fr.lehtto.jaser.dns.query.handler.QueryHandlerFactory;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for a client connection.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public class DnsClientHandler extends AbstractUdpClientHandler {

  private static final Logger LOG = LogManager.getLogger(DnsClientHandler.class);

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

      final byte[] buffer = new byte[512];
      final int length = ResponseWriter.write(QueryHandlerFactory.fromQuery(query).handle(query), buffer);
      getSocket().send(new DatagramPacket(buffer, length, getPacket().getAddress(), getPacket().getPort()));
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
}

