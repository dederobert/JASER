package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * DNS authority entity.
 *
 * @author lehtto
 * @since 0.1.0
 */
public class Authority extends ResourceRecord {

  /**
   * Valued constructor.
   *
   * @param name        the name of the resource record.
   * @param type        the type of the resource record.
   * @param recordClass the class of the resource record.
   * @param ttl         the time to live of the resource record.
   * @param data        the data of the resource record.
   */
  public Authority(final byte @NotNull [] name,
      final @NotNull Type type,
      final @NotNull DnsClass recordClass,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ttl,
      final @NotNull Rdata data) {
    super(name, type, recordClass, ttl, data);
  }
}
