package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.NamedRData;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * CNAME RDATA entity (RFC 1035 Section 3.3.1).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record CnameRdata(@NotNull DomainName cname) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    return cname.getBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return cname.getLength();
  }

  @Override
  public String toString() {
    return "CNAME RDATA{" + cname + '}';
  }

  @Override
  public @NotNull DomainName getName() {
    return cname;
  }

  /**
   * Parses the given string into a CNAME RDATA.
   */
  public static class CnameParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public CnameParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.CNAME != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("CNAME RDATA must have exactly one part");
      }
      return new CnameRdata(DomainName.of(parts[0]));
    }
  }
}
