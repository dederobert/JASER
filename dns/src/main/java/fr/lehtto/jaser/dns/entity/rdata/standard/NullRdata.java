package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

/**
 * NULL RDATA entity (RFC 1035, section 3.3.10) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record NullRdata(byte @NotNull [] data) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return Arrays.copyOf(data, data.length);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final NullRdata nullRdata = (NullRdata) o;
    return Arrays.equals(data, nullRdata.data);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(data);
  }

  @Override
  public String toString() {
    return "NullRdata{" +
        "data=" + Arrays.toString(data) +
        '}';
  }
}
