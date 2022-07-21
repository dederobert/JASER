package fr.lehtto.jaser.core;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Server.
 *
 * @param <H> the handler class
 * @author Lehtto
 * @version 0.2.0
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
  @OverrideOnly
  protected abstract void stop() throws IOException;

  /**
   * Starts the server and waits for connections.
   */
  @Override
  public final void run() {
    try {
      LOG.info("Starting server on {}:{}", getBindAddress(), getPort());
      startServer();
      LOG.info("TcpServer started on {}:{}", getBindAddress(), getPort());
      LOG.debug("Waiting for connections...");
      while (isRunning()) {
        final H clientHandler = acceptConnection();
        LOG.debug("New client connected");
        addClientHandler(clientHandler);
        final Thread thread = new Thread(clientHandler,
            "ClientHandler-" + clientHandler.getUuid().getMostSignificantBits());
        clientHandler.setThread(thread);
        thread.start();
      }
    } catch (final IOException e) {
      if (isRunning()) {
        LOG.error("Error while accepting client connection", e);
      }
    } catch (final InvocationTargetException e) {
      LOG.error("Error while creating client handler, check your handler class. "
          + "Handler constructor throws an exception", e);
    } catch (final InstantiationException e) {
      LOG.error("Error while creating client handler, could not instantiate class. "
          + "Hint: handler class must not be abstract", e);
    } catch (final IllegalAccessException e) {
      LOG.error("Error while creating client handler, illegal access", e);
    } catch (final NoSuchMethodException e) {
      LOG.error("Error while creating client handler, no constructor found. "
          + "Hint: handler class must have a public constructor with a single Socket parameter", e);
    }

    // Wait for all client handlers to finish
    LOG.debug("Waiting for client handlers to finish");
    for (final WeakReference<H> reference : getClientHandlers()) {
      final H clientHandler = reference.get();
      if (null != clientHandler) {
        final Thread thread = clientHandler.getThread();
        if (null != thread) {
          LOG.debug("Waiting for client handler {} to finish", reference);
          try {
            thread.join();
          } catch (final InterruptedException e) {
            LOG.error("Error while waiting for client handler to finish", e);
            thread.interrupt();
          }
        } else {
          LOG.warn("Client handler {} has no thread", clientHandler);
        }
      }
    }
    LOG.debug("All client handlers finished");
  }

  /**
   * Starts the server.
   *
   * @throws IOException if an error occurs
   */
  protected abstract void startServer() throws IOException;

  /**
   * Accepts a connection.
   *
   * @return the client handler
   * @throws IOException               if an error occurs
   * @throws InvocationTargetException if an error occurs
   * @throws InstantiationException    if an error occurs
   * @throws IllegalAccessException    if an error occurs
   * @throws NoSuchMethodException     if an error occurs
   */
  protected abstract H acceptConnection()
      throws IOException, InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException;

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
   * Adds a client handler.
   *
   * @param clientHandler the client handler
   */
  private void addClientHandler(@NotNull final H clientHandler) {
    clientHandlers.add(new WeakReference<>(clientHandler));
  }

  /**
   * Gets the client handlers.
   *
   * @return the client handlers
   */
  @UnmodifiableView
  @NotNull
  private Set<WeakReference<H>> getClientHandlers() {
    return Set.copyOf(clientHandlers);
  }

  /**
   * Checks if it is running.
   *
   * @return true, if it is running
   */
  private boolean isRunning() {
    return running;
  }
}
