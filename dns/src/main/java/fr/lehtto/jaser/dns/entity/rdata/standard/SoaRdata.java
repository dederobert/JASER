package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.core.utils.NumberUtils;
import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.MultiNamedRData;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * SOA RDATA entity (RFC 1035 section 3.3.13).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record SoaRdata(@NotNull DomainName origin, @NotNull DomainName contact, int serial, int refresh, int retry,
                       int expire, int minimum) implements Rdata, MultiNamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    final byte[] originBytes = origin.toBytes();
    final byte[] contactBytes = contact.toBytes();
    final ByteBuffer buffer = ByteBuffer.allocate(originBytes.length + contactBytes.length + 20);
    buffer.put(originBytes);
    buffer.put(contactBytes);
    buffer.putInt(serial);
    buffer.putInt(refresh);
    buffer.putInt(retry);
    buffer.putInt(expire);
    buffer.putInt(minimum);
    return buffer.array();
  }

  @Override
  public String toString() {
    return "SOA RDATA{" +
        "origin='" + origin + '\'' +
        ", contact='" + contact + '\'' +
        ", serial=" + serial +
        ", refresh=" + refresh +
        ", retry=" + retry +
        ", expire=" + expire +
        ", minimum=" + minimum +
        '}';
  }

  @Override
  public @NotNull DomainName[] getNames() {
    return new DomainName[]{origin, contact};
  }

  /**
   * Parses the given string into a SOA RDATA.
   */
  public static class SoaRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public SoaRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts) {
      if (Type.SOA != type) {
        return null;
      }
      if (7 != parts.length) {
        throw new IllegalArgumentException("SAO RDATA must have exactly 7 parts");
      }
      if (!NumberUtils.isParsable(parts[2])) {
        throw new IllegalArgumentException("Serial number must be a parsable integer");
      }
      if (!NumberUtils.isParsable(parts[3])) {
        throw new IllegalArgumentException("Refresh interval must be a parsable integer");
      }
      if (!NumberUtils.isParsable(parts[4])) {
        throw new IllegalArgumentException("Retry interval must be a parsable integer");
      }
      if (!NumberUtils.isParsable(parts[5])) {
        throw new IllegalArgumentException("Expire interval must be a parsable integer");
      }
      if (!NumberUtils.isParsable(parts[6])) {
        throw new IllegalArgumentException("Minimum TTL must be a parsable integer");
      }
      return new SoaRdata(DomainName.of(parts[0]), DomainName.of(parts[1]), Integer.parseInt(parts[2]),
          Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
          Integer.parseInt(parts[6]));
    }
  }
}
