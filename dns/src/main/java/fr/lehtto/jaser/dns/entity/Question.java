package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * DNS question.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Question(@NotNull String name, @NotNull Type type, @NotNull DnsClass recordClass)
    implements Writable {

  /**
   * {@inheritDoc}
   */
  @Override
  public byte @NotNull [] getBytes() {
    final byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final byte[] bytes = new byte[Stream.of(nameBytes, typeBytes, recordClassBytes).mapToInt(v -> v.length).sum() + 1];
    System.arraycopy(nameBytes, 0, bytes, 0, nameBytes.length);
    bytes[nameBytes.length] = 0; // 0x00
    System.arraycopy(typeBytes, 0, bytes, nameBytes.length + 1, typeBytes.length);
    System.arraycopy(recordClassBytes, 0, bytes, nameBytes.length + typeBytes.length + 1, recordClassBytes.length);
    return bytes;
  }
}
