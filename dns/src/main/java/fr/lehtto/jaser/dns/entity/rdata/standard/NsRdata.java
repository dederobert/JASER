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
 * NS RDATA entity (RFC 1035 Section 3.3.11).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record NsRdata(@NotNull DomainName nsdName) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    return nsdName.getBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return nsdName.getLength();
  }

  @Override
  public @NotNull DomainName getName() {
    return nsdName;
  }

  /**
   * Parses the given string into a NS RDATA.
   */
  public static class NsRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public NsRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.NS != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("NS RDATA must contain 1 part.");
      }
      return new NsRdata(DomainName.of(parts[0]));
    }
  }
}
