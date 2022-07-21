package fr.lehtto.jaser.core;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract client handler.
 *
 * @author Lehtto
 * @since 0.1.0
 */
abstract class AbstractClientHandler implements Closeable, Runnable {

  private static final Logger LOG =
      LoggerFactory.getLogger(AbstractClientHandler.class);

  private final @NotNull UUID uuid;
  private WeakReference<Thread> thread;
  private boolean running = true;

  /**
   * Default constructor.
   */
  AbstractClientHandler() { this.uuid = UUID.randomUUID(); }

  /**
   * Closes the client handler.
   *
   * @throws IOException if an error occurs
   */
  @Override
  public final void close() throws IOException {
    LOG.info("Closing client socket");
    running = false;
    stop();
  }

  /**
   * Stops the client handler.
   *
   * @throws IOException if an error occurs
   */
  @OverrideOnly protected abstract void stop() throws IOException;

  /**
   * Gets the running state.
   *
   * @return the running state
   */
  protected boolean isRunning() { return running; }

  /**
   * Gets the client ID.
   *
   * @return the client ID
   */
  @NotNull
  UUID getUuid() {
    return uuid;
  }

  /**
   * Gets the client handler thread.
   *
   * @return the client handler thread
   */
  @Nullable
  Thread getThread() {
    return thread.get();
  }

  /**
   * Sets the client handler thread.
   *
   * @param thread the client handler thread
   */
  void setThread(final @NotNull Thread thread) {
    this.thread = new WeakReference<>(thread);
  }
}
