package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * NS RDATA entity (RFC 1035 Section 3.3.11).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record NsRdata(@NotNull String nsdName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return nsdName.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public String toString() {
    return "NS RDATA{" + nsdName + '}';
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
      return new NsRdata(parts[0]);
    }
  }
}
