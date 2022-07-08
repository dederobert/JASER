package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * DNS resource record (RFC 1035 section 4.1.3).
 *
 * @author Lehtto
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public record ResourceRecord(
    boolean useCompression,
    short pointer,
    @NotNull DomainName name,
    @NotNull Type type,
    @NotNull DnsClass recordClass,
    @Range(from = 0, to = Integer.MAX_VALUE) int ttl,
    @NotNull Rdata data
) implements Writable {

  private static final Logger LOG = LogManager.getLogger(ResourceRecord.class);

  private static final int TTL_LENGTH = 4;
  private static final int LENGTH_RDATA_LENGTH = 2;

  /**
   * Creates a new builder for the resource record.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a new builder for the resource record.
   *
   * @return the new builder
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte @NotNull [] getBytes() {

    final byte[] nameBytes = name.toBytes();
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final byte[] dataBytes = data.getBytes();

    // Size of the buffer is the sum of the sizes of the fields.
    final int bufferSize = Stream.of(typeBytes, recordClassBytes, dataBytes).mapToInt(v -> v.length).sum()
        + TTL_LENGTH
        + LENGTH_RDATA_LENGTH
        + (useCompression ? 2 : nameBytes.length);

    final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
    if (useCompression) {
      byteBuffer.putShort(pointer);
    } else {
      byteBuffer.put(nameBytes);
    }
    byteBuffer.put(typeBytes);
    byteBuffer.put(recordClassBytes);
    byteBuffer.putInt(ttl);
    byteBuffer.putShort((short) dataBytes.length);
    byteBuffer.put(dataBytes);

    return byteBuffer.array();
  }

  /**
   * Builder for the {@link ResourceRecord resource record} entity.
   */
  @SuppressWarnings({"NumericCastThatLosesPrecision", "MagicNumber"})
  public static class Builder {

    private boolean useCompression;
    private Short pointer;
    private DomainName name;
    private Type type;
    private DnsClass recordClass;
    private int ttl;
    private Rdata data;

    /**
     * Default constructor.
     */
    Builder() {
      useCompression = false;
      pointer = null;
      name = null;
      type = null;
      recordClass = null;
      ttl = -1;
      data = null;
    }

    /**
     * Valued constructor.
     *
     * @param resourceRecord the resource record to copy.
     */
    Builder(final @NotNull ResourceRecord resourceRecord) {
      if (resourceRecord.useCompression()) {
        useCompression = true;
        pointer = resourceRecord.pointer();
        name = null;
      } else {
        useCompression = false;
        name = resourceRecord.name();
        pointer = null;
      }
      type = resourceRecord.type();
      recordClass = resourceRecord.recordClass();
      ttl = resourceRecord.ttl();
      data = resourceRecord.data();
    }

    /**
     * Sets pointer to the name of the resource record.
     *
     * @param offset the offset from the beginning of the packet.
     * @return the builder
     */
    @SuppressWarnings("NumericCastThatLosesPrecision")
    // TODO : refactor pointer mechanism
    public Builder pointer(final @Range(from = 0, to = 16_383) short offset) {
      this.useCompression = true;
      pointer = (short) (offset | 0xc000);
      return this;
    }

    /**
     * Sets the name of the resource record.
     *
     * @param name the name of the resource record.
     * @return the builder.
     */
    public Builder name(final @NotNull DomainName name) {
      this.useCompression = false;
      this.name = name;
      return this;
    }

    /**
     * Sets the type of the resource record.
     *
     * @param type the type of the resource record.
     * @return the builder.
     */
    public Builder type(final @NotNull Type type) {
      this.type = type;
      return this;
    }

    /**
     * Sets the class of the resource record.
     *
     * @param recordClass the class of the resource record.
     * @return this builder.
     */
    public Builder recordClass(final @NotNull DnsClass recordClass) {
      this.recordClass = recordClass;
      return this;
    }

    /**
     * Sets the time to live of the resource record.
     *
     * @param ttl the time to live to set
     * @return this builder
     */
    public Builder ttl(final @Range(from = 0, to = Integer.MAX_VALUE) int ttl) {
      this.ttl = ttl;
      return this;
    }

    /**
     * Sets the data of the resource record.
     *
     * @param data the data to set
     * @return this builder
     */
    public Builder data(final @NotNull Rdata data) {
      this.data = data;
      return this;
    }

    /**
     * Builds the {@link ResourceRecord resource record} entity.
     *
     * @return the {@link ResourceRecord resource record} entity.
     */
    public ResourceRecord build() {
      if (null == pointer) {
        if (useCompression) {
          throw new IllegalStateException("Pointer is not set while compression is enabled");
        }
         // HACK to avoid null pointer exception.
        pointer = 0;
      }
      if (null == name) {
        if (!useCompression) {
          throw new IllegalArgumentException("name is not set while compression is disabled");
        }
        // HACK to avoid null pointer exception.
        name = DomainName.of("");
      }
      if (null == type) {
        throw new IllegalArgumentException("type is not set.");
      }
      if (null == recordClass) {
        throw new IllegalArgumentException("recordClass is not set.");
      }
      if (-1 == ttl) {
        throw new IllegalArgumentException("ttl is not set.");
      }
      if (null == data) {
        throw new IllegalArgumentException("data is not set.");
      }
      return new ResourceRecord(useCompression, pointer, name, type, recordClass, ttl, data);
    }
  }
}
