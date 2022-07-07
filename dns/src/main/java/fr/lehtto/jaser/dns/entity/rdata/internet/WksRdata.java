package fr.lehtto.jaser.dns.entity.rdata.internet;

import fr.lehtto.jaser.dns.entity.AddressV4;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * WKS RDATA entity (RFC 1035, section 3.4.2).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record WksRdata(@NotNull AddressV4 address, byte protocol, byte @NotNull [] bitmap) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final byte @NotNull [] bytes = new byte[bitmap.length + 5];
    System.arraycopy(address.getBytes(), 0, bytes, 0, 4);
    bytes[4] = protocol;
    System.arraycopy(bitmap, 0, bytes, 5, bitmap.length);
    return bytes;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final WksRdata wksRdata = (WksRdata) o;
    return protocol == wksRdata.protocol && Objects.equals(address, wksRdata.address) && Arrays.equals(
        bitmap, wksRdata.bitmap);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(protocol);
    result = 31 * result + Objects.hashCode(address);
    result = 31 * result + Arrays.hashCode(bitmap);
    return result;
  }

  @Override
  public String toString() {
    return "WksRdata{" +
        "address=" + address +
        ", protocol=" + protocol +
        ", bitmap=" + Arrays.toString(bitmap) +
        '}';
  }

  /**
   * Parses the given string into a WKS RDATA.
   */
  public static class WksRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public WksRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts) {
      return null;
    }
  }
}
