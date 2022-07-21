package fr.lehtto.jaser.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tcp server.
 *
 * @author Lehtto
 * @version 0.2.0
 * @since 0.1.0
 */
@Experimental
public class TcpServer<H extends AbstractTcpClientHandler> extends Server<H> {

  private @Nullable ServerSocket serverSocket;

  /**
   * Valued constructor.
   *
   * @param port         the port
   * @param bindAddress  the bind address
   * @param handlerClass the handler class
   */
  public TcpServer(final int port, final @NotNull InetAddress bindAddress, final @NotNull Class<H> handlerClass) {
    super(port, bindAddress, handlerClass);
  }

  @Override
  protected void startServer() throws IOException {
    serverSocket = new ServerSocket(getPort(), 0, getBindAddress());
  }

  @Override
  protected H acceptConnection()
      throws IOException, InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    assert null != serverSocket;
    return getHandlerClass().getDeclaredConstructor(Socket.class)
        .newInstance(serverSocket.accept());
  }

  /**
   * Stops the server.
   *
   * @throws IOException if an error occurs
   */
  @Override
  public void stop() throws IOException {
    if (null != serverSocket) {
      serverSocket.close();
    }
  }
}
