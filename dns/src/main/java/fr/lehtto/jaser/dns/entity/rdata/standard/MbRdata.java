package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MB RDATA entity (RFC 1035, section 3.3.3) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MbRdata(@NotNull String madname) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return madname.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Parses the given string into an MB RDATA.
   */
  public static class MbRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MbRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MB != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("MB RDATA must have exactly one part");
      }
      return new MbRdata(parts[0]);
    }
  }

}
