package fr.lehtto.jaser.core;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Server.
 *
 * @param <H> the handler class
 * @author Lehtto
 * @since 0.1.0
 */
abstract class Server<H extends AbstractClientHandler> implements AutoCloseable, Runnable {

  private static final Logger LOG = LogManager.getLogger(Server.class);

  private final int port;
  private final @NotNull InetAddress bindAddress;
  private final @NotNull Class<H> handlerClass;
  private final @NotNull Set<WeakReference<H>> clientHandlers = new HashSet<>();
  private boolean running = true;

  /**
   * Valued constructor.
   *
   * @param port         the port
   * @param bindAddress  the bind address
   * @param handlerClass the handler class
   */
  protected Server(final int port, @NotNull final InetAddress bindAddress,
      @NotNull final Class<H> handlerClass) {
    this.port = port;
    this.bindAddress = bindAddress;
    this.handlerClass = handlerClass;
  }

  /**
   * Closes the server.
   *
   * @throws IOException if an error occurs
   */
  @Override
  public final void close() throws IOException {
    LOG.info("Closing server");
    running = false;
    stop();
    for (final WeakReference<H> reference : getClientHandlers()) {
      final H clientHandler = reference.get();
      if (null != clientHandler) {
        LOG.debug("Closing client handler {}", clientHandler);
        clientHandler.close();
      } else {
        LOG.debug("Client handler {} already closed", reference);
      }
    }
  }

  /**
   * Stops the server.
   *
   * @throws IOException if an error occurs
   */
  protected abstract void stop() throws IOException;

  /**
   * Adds a client handler.
   *
   * @param clientHandler the client handler
   */
  void addClientHandler(@NotNull final H clientHandler) {
    clientHandlers.add(new WeakReference<>(clientHandler));
  }

  /**
   * Gets the port.
   *
   * @return the port
   */
  int getPort() {
    return port;
  }

  /**
   * Gets the bind address.
   *
   * @return the bind address
   */
  @NotNull InetAddress getBindAddress() {
    return bindAddress;
  }

  /**
   * Gets the handler class.
   *
   * @return the handler class
   */
  @NotNull Class<H> getHandlerClass() {
    return handlerClass;
  }

  /**
   * Gets the client handlers.
   *
   * @return the client handlers
   */
  @UnmodifiableView
  @NotNull Set<WeakReference<H>> getClientHandlers() {
    return Set.copyOf(clientHandlers);
  }

  /**
   * Checks if it is running.
   *
   * @return true, if it is running
   */
  boolean isRunning() {
    return running;
  }
}
