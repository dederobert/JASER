package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * MINFO RDATA entity (RFC 1035, section 3.3.7) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MinfoRdata(@NotNull String rmailbx, @NotNull String emailbx) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final ByteBuffer buffer = ByteBuffer.allocate(rmailbx.length() + emailbx.length());
    buffer.put(rmailbx.getBytes(StandardCharsets.UTF_8));
    buffer.put(emailbx.getBytes(StandardCharsets.UTF_8));
    return buffer.array();
  }
}
