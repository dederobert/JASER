package fr.lehtto.jaser.dns.metrics;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.HTTPServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service for metrics.
 *
 * @author Lehtto
 * @version 0.2.0
 */
public class MetricsService implements AutoCloseable {

  private static final Logger LOG = LogManager.getLogger(MetricsService.class);
  private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
  private final InetAddress address;
  private final int port;
  private final Metrics metrics;
  // region metrics
  private final ClassLoaderMetrics classLoaderMetrics = new ClassLoaderMetrics();
  private final JvmMemoryMetrics jvmMemoryMetrics = new JvmMemoryMetrics();
  private final JvmGcMetrics jvmGcMetrics = new JvmGcMetrics();
  private final ProcessorMetrics processorMetrics = new ProcessorMetrics();
  private final JvmThreadMetrics jvmThreadMetrics = new JvmThreadMetrics();
  private HTTPServer server;
  // endregion

  /**
   * Valued constructor.
   *
   * @param address AddressV4 of the metrics server
   * @param port    Port of the metrics server
   */
  public MetricsService(final InetAddress address, final int port) {
    this.address = address;
    this.port = port;
    metrics = new Metrics(registry);
  }

  /**
   * Starts the metrics server.
   */
  public void start() {
    setUpMetrics();

    // Start the metrics server
    try {
      LOG.info("Starting metrics server on {}:{}", address, port);
      server = new HTTPServer(new InetSocketAddress(address, port), registry.getPrometheusRegistry(), true);
    } catch (final IOException e) {
      LOG.error("Error while starting metrics server", e);
    }
  }

  @Override
  public void close() {
    LOG.info("Stopping metrics server");
    jvmGcMetrics.close();
    server.close();
    registry.close();
  }

  /**
   * Gets the metrics.
   *
   * @return the metrics
   */
  public Metrics getMetrics() {
    return metrics;
  }

  /**
   * Sets up the metrics.
   */
  private void setUpMetrics() {
    LOG.info("Setting up metrics");
    classLoaderMetrics.bindTo(registry);
    jvmMemoryMetrics.bindTo(registry);
    jvmGcMetrics.bindTo(registry);
    processorMetrics.bindTo(registry);
    jvmThreadMetrics.bindTo(registry);
  }
}
