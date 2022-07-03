package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * CNAME RDATA entity (RFC 1035 Section 3.3.1).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record CnameRdata(@NotNull String cname) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return cname.getBytes(StandardCharsets.UTF_8);
  }
}
