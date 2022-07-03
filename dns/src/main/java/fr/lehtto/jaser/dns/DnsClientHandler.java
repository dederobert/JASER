package fr.lehtto.jaser.dns;

import fr.lehtto.jaser.core.AbstractUdpClientHandler;
import fr.lehtto.jaser.dns.entity.AddressV4;
import fr.lehtto.jaser.dns.entity.Answer;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Response;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.internet.ARdata;
import fr.lehtto.jaser.dns.entity.writer.ResponseWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for a client connection.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
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
      final int length = ResponseWriter.write(handleQuery(query), buffer);
      getSocket().send(new DatagramPacket(buffer, length, getPacket().getAddress(), getPacket().getPort()));
    } catch (final Exception e) {
      if (isRunning()) {
        LOG.error("Error while handling client", e);
      }
    }
    LOG.info("Client disconnected from {}", getPacket().getAddress());
  }

  /**
   * Handles a query.
   *
   * @param query the query
   * @return the response
   */
  private Response handleQuery(final Query query) {
    // TODO: handle query

    final Answer answer = new Answer(
        // pointer to name
        new byte[]{
            (byte) 0xc0,
            (byte) 0xc,
        },
        Type.A,
        DnsClass.IN,
        116,
        new ARdata(AddressV4.of(172, 217, 19, 228))
    );

    // TODO update response QR bit and AN count
    return new Response(query.header(), query.questions(), Set.of(answer), Set.of(), Set.of());
  }

  @Override
  protected void stop() {
    // Do nothing
  }
}

