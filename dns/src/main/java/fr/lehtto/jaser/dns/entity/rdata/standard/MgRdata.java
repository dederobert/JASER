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
 * MG RDATA entity (RFC 1035, section 3.3.6) (Experimental).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record MgRdata(@NotNull DomainName mgmName) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    return mgmName.getBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return mgmName.getLength();
  }

  @Override
  public @NotNull DomainName getName() {
    return mgmName;
  }

  /**
   * Parses the given string into a MG RDATA.
   */
  public static class MgRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MgRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MG != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("MG RDATA must contain exactly 1 part.");
      }
      return new MgRdata(DomainName.of(parts[0]));
    }
  }
}
