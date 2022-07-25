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
 * MF RDATA entity (RFC 1035, section 3.3.5) (Obsolete).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MfRdata(@NotNull DomainName madname) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    return madname.toBytes();
  }

  @Override
  public @NotNull DomainName getName() {
    return madname;
  }

  /**
   * Parses the given string into a MF RDATA.
   */
  public static class MfRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MfRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MF != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("MF RDATA must contain exactly 1 part.");
      }
      return new MfRdata(DomainName.of(parts[0]));
    }
  }

}
