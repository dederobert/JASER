package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * SOA RDATA entity (RFC 1035 section 3.3.13).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record SoaRdata(@NotNull String mName, @NotNull String rName, int serial, int refresh, int retry, int expire,
                       int minimum) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final ByteBuffer buffer = ByteBuffer.allocate(mName.length() + rName.length() + 20);
    buffer.put(mName.getBytes(StandardCharsets.UTF_8));
    buffer.put(rName.getBytes(StandardCharsets.UTF_8));
    buffer.putInt(serial);
    buffer.putInt(refresh);
    buffer.putInt(retry);
    buffer.putInt(expire);
    buffer.putInt(minimum);
    return buffer.array();
  }
}
