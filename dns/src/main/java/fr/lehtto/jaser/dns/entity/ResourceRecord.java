package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * DNS resource record (RFC 1035 section 4.1.3).
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
abstract class ResourceRecord implements Writable {

  private final byte @NotNull [] name;
  private final @NotNull Type type;
  private final @NotNull DnsClass recordClass;
  private final @Range(from = 0, to = Integer.MAX_VALUE) int ttl;
  private final @NotNull Rdata data;

  /**
   * Valued constructor.
   *
   * @param name the name of the resource record.
   * @param type the type of the resource record.
   * @param recordClass the class of the resource record.
   * @param ttl the time to live of the resource record.
   * @param data the data of the resource record.
   */
  ResourceRecord(
      final byte @NotNull [] name,
      final @NotNull Type type,
      final @NotNull DnsClass recordClass,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ttl,
      final @NotNull Rdata data
  ) {
    this.name = name;
    this.type = type;
    this.recordClass = recordClass;
    this.ttl = ttl;
    this.data = data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte @NotNull [] getBytes() {

    final byte[] nameBytes = name;
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final byte[] dataBytes = data.getBytes();
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
    final ResourceRecord answer = (ResourceRecord) o;
    return ttl == answer.ttl && Arrays.equals(name, answer.name) && type == answer.type
        && recordClass == answer.recordClass && Objects.equals(data, answer.data);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(type, recordClass, ttl);
    result = 31 * result + Arrays.hashCode(name);
    result = 31 * result + Objects.hashCode(data);
    return result;
  }

  @Override
  public String toString() {
    return "ResourceRecord{" +
        "name=" + Arrays.toString(name) +
        ", type=" + type +
        ", recordClass=" + recordClass +
        ", ttl=" + ttl +
        ", data=" + data +
        '}';
  }

  /**
   * Gets the name of the resource record.
   *
   * @return the name of the resource record.
   */
  public byte @NotNull [] name() {
    return name;
  }

  /**
   * Gets the type of the resource record.
   *
   * @return the type of the resource record.
   */
  public @NotNull Type type() {
    return type;
  }

  /**
   * Gets the class of the resource record.
   *
   * @return the class of the resource record.
   */
  public @NotNull DnsClass recordClass() {
    return recordClass;
  }

  /**
   * Gets the time to live of the resource record.
   *
   * @return the time to live of the resource record.
   */
  public @Range(from = 0, to = Integer.MAX_VALUE) int ttl() {
    return ttl;
  }

  /**
   * Gets the data of the resource record.
   *
   * @return the data of the resource record.
   */
  public @NotNull Rdata data() {
    return data;
  }

}
