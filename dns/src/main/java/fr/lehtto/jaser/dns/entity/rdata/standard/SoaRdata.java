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
 * @version 1.0.0
 * @since 0.1.0
 */
public record SoaRdata(@NotNull DomainName origin, @NotNull DomainName contact, int serial, int refresh, int retry,
                       int expire, int minimum) implements Rdata, MultiNamedRData {

  private static final int SERIAL_LENGTH = 4;
  private static final int REFRESH_LENGTH = 4;
  private static final int RETRY_LENGTH = 4;
  private static final int EXPIRE_LENGTH = 4;
  private static final int MINIMUM_LENGTH = 4;

  @Override
  public byte @NotNull [] getBytes() {
    final byte[] originBytes = origin.getBytes();
    final byte[] contactBytes = contact.getBytes();
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

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    //noinspection OverlyComplexArithmeticExpression
    return origin.getLength() + contact.getLength() + SERIAL_LENGTH + REFRESH_LENGTH + RETRY_LENGTH + EXPIRE_LENGTH
        + MINIMUM_LENGTH;
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
