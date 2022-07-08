package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MD RDATA entity (RFC 1035, section 3.3.4) (Obsolete).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MdRdata(@NotNull DomainName madname) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return madname.toBytes();
  }

  /**
   * Parses the given string into an MD RDATA.
   */
  public static class MdRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MdRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MD != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("MD RDATA must contain exactly 1 part.");
      }
      return new MdRdata(DomainName.of(parts[0]));
    }
  }

}
