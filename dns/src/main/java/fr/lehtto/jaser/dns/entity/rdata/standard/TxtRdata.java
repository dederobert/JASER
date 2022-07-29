package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TXT RDATA entity (RFC1035 section 3.3.14).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record TxtRdata(@NotNull String txt) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return txt.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return txt.length();
  }

  @Override
  public String toString() {
    return "TXT RDATA{" + txt + '}';
  }

  /**
   * Parses the given string into a TXT RDATA.
   */
  public static class TxtRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public TxtRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts) {
      if (Type.TXT != type) {
        return null;
      }
      return new TxtRdata(String.join(" ", parts));
    }
  }
}
