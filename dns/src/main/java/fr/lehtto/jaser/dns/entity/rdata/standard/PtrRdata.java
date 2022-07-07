package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PTR RDATA entity (RFC 1035 section 3.3.12).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record PtrRdata(@NotNull String ptrdName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return ptrdName.getBytes(StandardCharsets.UTF_8);
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
      return new PtrRdata(parts[0]);
    }
  }
}
