package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * MG RDATA entity (RFC 1035, section 3.3.6) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MgRdata(@NotNull String mgmName) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return mgmName.getBytes(StandardCharsets.UTF_8);
  }

}
