package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * TXT RDATA entity (RFC1035 section 3.3.14).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record TxtRdata(@NotNull String txt) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    return txt.getBytes(StandardCharsets.UTF_8);
  }
}
