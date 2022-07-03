package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * MD RDATA entity (RFC 1035, section 3.3.4) (Obsolete).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MdRdata(@NotNull String madname) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return madname.getBytes(StandardCharsets.UTF_8);
  }

}
