package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.core.utils.NumberUtils;
import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.NamedRData;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MX RDATA entity (RFC 1035, section 3.3.9).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record MxRdata(short preference, @NotNull DomainName exchange) implements Rdata, NamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    final byte[] exchangeBytes = exchange.getBytes();
    final ByteBuffer buffer = ByteBuffer.allocate(2 + exchangeBytes.length);
    buffer.putShort(preference);
    buffer.put(exchangeBytes);
    return buffer.array();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return 2 + exchange.getLength();
  }

  @Override
  public String toString() {
    return "MX RDATA{" +
        "preference=" + preference +
        ", exchange='" + exchange + '\'' +
        '}';
  }

  @Override
  public @NotNull DomainName getName() {
    return exchange;
  }

  /**
   * Parses the given string into a MX RDATA.
   */
  public static class MxRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MxRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
        throws InvalidDnsZoneEntryException {
      if (Type.MX != type) {
        return null;
      }
      if (2 != parts.length) {
        throw new InvalidDnsZoneEntryException("MX RDATA must contain 2 parts.");
      }
      if (!NumberUtils.isParsable(parts[0])) {
        throw new InvalidDnsZoneEntryException("MX RDATA must contain a valid number for the preference. Contains %s",
            parts[0]);
      }
      return new MxRdata(Short.parseShort(parts[0]), DomainName.of(parts[1]));
    }
  }
}
