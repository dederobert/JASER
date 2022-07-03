package fr.lehtto.jaser.core;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DNS server.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@Experimental
public class UdpServer<H extends AbstractUdpClientHandler> extends Server<H> {

  private static final Logger LOG = LogManager.getLogger(UdpServer.class);

  private @Nullable DatagramSocket datagramSocket;

  /**
   * Valued constructor.
   *
   * @param port         the port
   * @param bindAddress  the bind address
   * @param handlerClass the handler class
   */
  public UdpServer(final int port, final @NotNull InetAddress bindAddress, final @NotNull Class<H> handlerClass) {
    super(port, bindAddress, handlerClass);
  }

  /**
   * Starts the server and waits for connections.
   */
  @Override
  public void run() {
    try {
      LOG.info("Starting server on {}:{}", getBindAddress(), getPort());
      datagramSocket = new DatagramSocket(getPort(), getBindAddress());
      LOG.info("TcpServer started on {}:{}", getBindAddress(), getPort());
      LOG.debug("Waiting for connections...");
      while (isRunning()) {
        final DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
        datagramSocket.receive(datagramPacket);
        // Create a new client handler
        final H clientHandler = getHandlerClass()
            .getDeclaredConstructor(DatagramPacket.class, DatagramSocket.class)
            .newInstance(datagramPacket, datagramSocket);
        LOG.debug("New client connected from {}", datagramPacket.getAddress().getHostAddress());
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
          LOG.trace("Waiting for client handler {} to finish", clientHandler);
          try {
            thread.join();
          } catch (final InterruptedException e) {
            LOG.warn("Error while waiting for client handler to finish", e);
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
   * Closes the server.
   *
   */
  @Override
  public void stop() {
    if (null != datagramSocket) {
      datagramSocket.close();
    }
  }
}
