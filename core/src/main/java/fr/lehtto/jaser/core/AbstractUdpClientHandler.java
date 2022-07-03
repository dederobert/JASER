package fr.lehtto.jaser.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for a client connection.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@Experimental
public abstract class AbstractUdpClientHandler extends AbstractClientHandler {

  private final @NotNull DatagramPacket packet;
  private final @NotNull DatagramSocket socket;

  /**
   * Valued constructor.
   *
   * @param packet the client packet
   * @param socket the server socket
   */
  protected AbstractUdpClientHandler(final @NotNull DatagramPacket packet,
      @NotNull final DatagramSocket socket) {
    this.packet = packet;
    this.socket = socket;
  }

  /**
   * Gets the client packet.
   *
   * @return the client packet
   */
  protected @NotNull DatagramPacket getPacket() {
    return packet;
  }

  /**
   * Gets the server socket.
   *
   * @return the server socket
   */
  protected @NotNull DatagramSocket getSocket() {
    return socket;
  }
}

