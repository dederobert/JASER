package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * MX RDATA entity (RFC 1035, section 3.3.9).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MxRdata(short preference, @NotNull String exchange) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final ByteBuffer buffer = ByteBuffer.allocate(2 + exchange.length());
    buffer.putShort(preference);
    buffer.put(exchange.getBytes(StandardCharsets.UTF_8));
    return buffer.array();
  }
}
