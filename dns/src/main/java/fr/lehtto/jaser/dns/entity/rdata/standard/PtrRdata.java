package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * PTR RDATA entity (RFC 1035 section 3.3.12).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record PtrRdata(@NotNull String ptrdName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return ptrdName.getBytes(StandardCharsets.UTF_8);
  }
}
