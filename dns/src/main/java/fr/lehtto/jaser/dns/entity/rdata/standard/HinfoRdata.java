package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * HINFO RDATA entity (RFC 1035, section 3.3.2).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record HinfoRdata(@NotNull String cpu, @NotNull String os) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final ByteBuffer buffer = ByteBuffer.allocate(cpu.length() + os.length());
    buffer.put(cpu.getBytes(StandardCharsets.UTF_8));
    buffer.put(os.getBytes(StandardCharsets.UTF_8));
    return buffer.array();
  }
}
