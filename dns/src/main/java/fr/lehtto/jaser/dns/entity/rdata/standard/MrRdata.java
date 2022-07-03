package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * MR RDATA entity (RFC 1035, section 3.3.8) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MrRdata(@NotNull String newName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return newName.getBytes(StandardCharsets.UTF_8);
  }
}
