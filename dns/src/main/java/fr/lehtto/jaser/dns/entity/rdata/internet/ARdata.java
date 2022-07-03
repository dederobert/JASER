package fr.lehtto.jaser.dns.entity.rdata.internet;

import fr.lehtto.jaser.dns.entity.AddressV4;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A RDATA entity (RFC 1035 section 3.4.1).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record ARdata(@NotNull AddressV4 address) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return address.getBytes();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final ARdata aRdata = (ARdata) o;
    return Objects.equals(address, aRdata.address);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(address);
  }

  @Override
  public String toString() {
    return "ARdata{" +
        "address=" + address +
        '}';
  }
}
