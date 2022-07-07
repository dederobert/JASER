package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * HINFO RDATA entity (RFC 1035, section 3.3.2).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record HinfoRdata(@NotNull String hardware, @NotNull String software) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final ByteBuffer buffer = ByteBuffer.allocate(hardware.length() + software.length());
    buffer.put(hardware.getBytes(StandardCharsets.UTF_8));
    buffer.put(software.getBytes(StandardCharsets.UTF_8));
    return buffer.array();
  }

  @Override
  public String toString() {
    return "HINFO RDATA{" +
        "hardware='" + hardware + '\'' +
        ", software='" + software + '\'' +
        '}';
  }

  /**
   * Parses the given string into a HINFO RDATA.
   */
  public static class HinfoParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public HinfoParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.HINFO != type) {
        return null;
      }
      final String text = String.join(" ", parts);
      final Pattern pattern = Pattern.compile("\"[^\"]*\"");
      final String[] split = pattern.matcher(text).results().map(MatchResult::group).toArray(String[]::new);
      if (2 != split.length) {
        throw new InvalidDnsZoneEntryException("HINFO RDATA must contain 2 parts");
      }
      return new HinfoRdata(split[0], split[1]);
    }
  }
}
