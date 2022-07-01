package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * DNS answer.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final record Answer(byte @NotNull [] name, @NotNull Type type, @NotNull DnsClass recordClass, int ttl,
                           byte @NotNull [] data) implements Writable {

  /**
   * {@inheritDoc}
   */
  @Override
  public byte @NotNull [] getBytes() {

    final byte[] nameBytes = name;
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final byte[] dataBytes = data;
    final byte[] bytes = new byte[
        Stream.of(nameBytes, typeBytes, recordClassBytes, dataBytes).mapToInt(v -> v.length).sum() + 6];

    final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    byteBuffer.put(nameBytes);
    byteBuffer.put(typeBytes);
    byteBuffer.put(recordClassBytes);
    byteBuffer.putInt(ttl);
    byteBuffer.putShort((short) dataBytes.length);
    byteBuffer.put(dataBytes);

    return bytes;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final Answer answer = (Answer) o;
    return ttl == answer.ttl && Arrays.equals(name, answer.name) && type == answer.type
        && recordClass == answer.recordClass && Arrays.equals(data, answer.data);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(type, recordClass, ttl);
    result = 31 * result + Arrays.hashCode(name);
    result = 31 * result + Arrays.hashCode(data);
    return result;
  }

  @Override
  public String toString() {
    return "Answer{" +
        "name=" + Arrays.toString(name) +
        ", type=" + type +
        ", recordClass=" + recordClass +
        ", ttl=" + ttl +
        ", data=" + Arrays.toString(data) +
        '}';
  }
}
