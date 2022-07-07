package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MR RDATA entity (RFC 1035, section 3.3.8) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MrRdata(@NotNull String newName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return newName.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Parses the given string into a MRR RDATA.
   */
  public static class MrRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MrRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MR != type) {
        return null;
      }
      if (1 != parts.length) {
        throw new InvalidDnsZoneEntryException("MR RDATA must contain exactly 1 part.");
      }
      return new MrRdata(parts[0]);
    }
  }
}
