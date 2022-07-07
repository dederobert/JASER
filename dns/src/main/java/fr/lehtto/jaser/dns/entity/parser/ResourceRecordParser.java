package fr.lehtto.jaser.dns.entity.parser;

import fr.lehtto.jaser.core.utils.EnumUtils;
import fr.lehtto.jaser.core.utils.NumberUtils;
import fr.lehtto.jaser.core.utils.StringUtils;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.RDataFactory;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.util.Arrays;
import org.jetbrains.annotations.Contract;

/**
 * Parses a DNS resource record.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public final class ResourceRecordParser {

  /**
   * Default constructor.
   */
  private ResourceRecordParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Parses a DNS resource record.
   *
   * @param line the line to parse
   * @return the parsed resource record
   * @throws InvalidDnsZoneEntryException if the line is not a valid DNS zone entry
   */
  @Contract("null -> fail")
  public static ResourceRecord parse(final String line) throws InvalidDnsZoneEntryException {
    if (null == line) {
      throw new InvalidDnsZoneEntryException("Line cannot be null.");
    }
    final String[] parts = line.split(" ");

    if (5 > parts.length) {
      throw new InvalidDnsZoneEntryException("Line '%s' must contain at least 5 parts.", line);
    }
    if (StringUtils.isEmpty(parts[0])) {
      throw new InvalidDnsZoneEntryException("Line '%s' must contain a name.", line);
    }
    if (!NumberUtils.isParsable(parts[1])) {
      throw new InvalidDnsZoneEntryException("Line '%s' must contain a valid number for the TTL. Contains '%s'", line,
          parts[1]);
    }
    if (!EnumUtils.isValidEnum(DnsClass.class, parts[2])) {
      throw new InvalidDnsZoneEntryException("Line '%s' must contain a valid DNS class. Contains '%s'", line, parts[2]);
    }
    if (!EnumUtils.isValidEnum(Type.class, parts[3])) {
      throw new InvalidDnsZoneEntryException("Line '%s' must contain a valid type. Contains '%s'", line, parts[3]);
    }

    final String name = parts[0];
    final int ttl = Integer.parseInt(parts[1]);
    final DnsClass clazz = DnsClass.valueOf(parts[2]);
    final Type type = Type.valueOf(parts[3]);
    final String[] rdata = Arrays.copyOfRange(parts, 4, parts.length);
    final Rdata rdata1 = RDataFactory.create(type, rdata);

    return ResourceRecord.builder()
        .name(name)
        .type(type)
        .recordClass(clazz)
        .ttl(ttl)
        .data(rdata1)
        .build();
  }
}
