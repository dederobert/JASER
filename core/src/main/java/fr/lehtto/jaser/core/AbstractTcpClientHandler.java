package fr.lehtto.jaser.core;

import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for a client connection.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@Experimental
public abstract class AbstractTcpClientHandler extends AbstractClientHandler {

  private final @NotNull Socket socket;

  /**
   * Valued constructor.
   *
   * @param socket the client socket
   */
  protected AbstractTcpClientHandler(final @NotNull Socket socket) {
    this.socket = socket;
  }

  /**
   * Closes the client socket.
   *
   * @throws IOException if an error occurs
   */
  @Override
  public void stop() throws IOException {
    socket.close();
  }

  /**
   * Gets the client socket.
   *
   * @return the client socket
   */
  protected @NotNull Socket getSocket() {
    return socket;
  }
}

