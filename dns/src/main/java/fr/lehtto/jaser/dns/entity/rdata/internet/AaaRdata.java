package fr.lehtto.jaser.dns.entity.rdata.internet;

import fr.lehtto.jaser.dns.entity.AddressV6;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AAA RDATA entity (RFC 1035 section 3.4.1).
 *
 * @author lehtto
 * @since 0.1.0
 * @version 1.0.0
 */
public record AaaRdata(@NotNull AddressV6 address) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return address.getBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return address.getLength();
  }

  /**
   * Parses the given string into an AAA RDATA.
   */
  public static class AaaRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public AaaRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.AAA != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("RDATA for A type must contain exactly 1 part", parts);
      }
      return new AaaRdata(AddressV6.of(parts[0]));
    }
  }
}
