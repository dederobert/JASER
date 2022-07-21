package fr.lehtto.jaser.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DNS server.
 *
 * @author Lehtto
 * @version 0.2.0
 * @since 0.1.0
 */
@Experimental
public class UdpServer<H extends AbstractUdpClientHandler> extends Server<H> {

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

  @Override
  protected void startServer() throws SocketException {
    datagramSocket = new DatagramSocket(getPort(), getBindAddress());
  }

  @Override
  protected H acceptConnection()
      throws IOException, InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    final DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
    assert null != datagramSocket;
    datagramSocket.receive(datagramPacket);
    // Create a new client handler
    return getHandlerClass()
        .getDeclaredConstructor(DatagramPacket.class, DatagramSocket.class)
        .newInstance(datagramPacket, datagramSocket);

  }

  /**
   * Closes the server.
   */
  @Override
  public void stop() {
    if (null != datagramSocket) {
      datagramSocket.close();
    }
  }
}
