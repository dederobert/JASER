package fr.lehtto.jaser.dns.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Metrics.
 *
 * @author Lehtto
 * @version 0.2.0
 */
public class Metrics {

  private final Counter zoneSize;
  private final Counter aQuery;
  private final Counter nsQuery;
  private final Counter cnameQuery;
  private final Timer queryTimer;

  /**
   * Valued constructor.
   *
   * @param registry MeterRegistry to use
   */
  Metrics(final MeterRegistry registry) {
    zoneSize = Counter.builder("jaser_dns_zone_size").description("Size of the DNS zone").register(registry);
    aQuery = Counter.builder("jaser_dns_query").tag("type", "A").register(registry);
    nsQuery = Counter.builder("jaser_dns_query").tag("type", "NS").register(registry);
    cnameQuery = Counter.builder("jaser_dns_query").tag("type", "CNAME").register(registry);
    queryTimer = Timer.builder("jaser_dns_query_time").register(registry);
  }

  /**
   * Increments the zone size.
   *
   * @param size how much to increment the zone size by
   */
  public void incrementZoneSize(final double size) {
    zoneSize.increment(size);
  }

  /**
   * Increments the A query counter.
   *
   * @param number how much to increment the counter by
   */
  public void incrementAQuery(final double number) {
    aQuery.increment(number);
  }

  /**
   * Increments the NS query counter.
   *
   * @param number how much to increment the counter by
   */
  public void incrementNsQuery(final double number) {
    nsQuery.increment(number);
  }

  /**
   * Increments the CNAME query counter.
   *
   * @param number how much to increment the counter by
   */
  public void incrementCnameQuery(final double number) {
    cnameQuery.increment(number);
  }

  /**
   * Gets the query timer.
   *
   * @return the query timer
   */
  public Timer getQueryTimer() {
    return queryTimer;
  }
}
