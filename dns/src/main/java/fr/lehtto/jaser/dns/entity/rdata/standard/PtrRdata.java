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
 * PTR RDATA entity (RFC 1035 section 3.3.12).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record PtrRdata(@NotNull DomainName ptrdName) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    return ptrdName.getBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return ptrdName.getLength();
  }

  @Override
  public @NotNull DomainName getName() {
    return ptrdName;
  }

  /**
   * Parses the given string into a PTR RDATA.
   */
  public static class PtrRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public PtrRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.PTR != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("PTR RDATA must contain exactly 1 part.");
      }
      return new PtrRdata(DomainName.of(parts[0]));
    }
  }
}
