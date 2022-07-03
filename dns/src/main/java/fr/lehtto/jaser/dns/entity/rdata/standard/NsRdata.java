package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * NS RDATA entity (RFC 1035 Section 3.3.11).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record NsRdata(@NotNull String nsdName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return nsdName.getBytes(StandardCharsets.UTF_8);
  }
}
