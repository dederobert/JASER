package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * DNS resource record (RFC 1035 section 4.1.3).
 *
 * @author Lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final class ResourceRecord implements Writable {

  private static final int TTL_LENGTH = 4;
  private static final int LENGTH_RDATA_LENGTH = 2;
  private static final int POINTER_LENGTH = 2;
  private static final short POINTER_MASK = (short) 0xc000;
  private final boolean useCompression;
  private final Writable pointer;
  private final @NotNull DomainName name;
  private final @NotNull Type type;
  private final @NotNull DnsClass recordClass;
  private final @Range(from = 0, to = Integer.MAX_VALUE) int ttl;
  private final @NotNull Rdata data;
  private short pointerShort = -1;

  /**
   * Valued constructor.
   *
   * @param name        the name of the resource record
   * @param type        the type of the resource record
   * @param recordClass the class of the resource record
   * @param ttl         the time to live of the resource record
   * @param data        the data of the resource record
   */
  public ResourceRecord(final @NotNull DomainName name,
      final @NotNull Type type,
      final @NotNull DnsClass recordClass,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ttl,
      final @NotNull Rdata data) {
    this.useCompression = false;
    this.pointer = null;
    this.name = name;
    this.type = type;
    this.recordClass = recordClass;
    this.ttl = ttl;
    this.data = data;
  }

  /**
   * Valued constructor.
   *
   * @param pointer     the pointer to the name (if useCompression is true)
   * @param type        the type of the resource record
   * @param recordClass the class of the resource record
   * @param ttl         the time to live of the resource record
   * @param data        the data of the resource record
   * @since 1.0.0
   */
  public ResourceRecord(final Writable pointer, final @NotNull Type type,
      final @NotNull DnsClass recordClass,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ttl,
      final @NotNull Rdata data) {
    this.useCompression = true;
    this.pointer = pointer;
    this.name = DomainName.of("");
    this.type = type;
    this.recordClass = recordClass;
    this.ttl = ttl;
    this.data = data;
  }

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

    final byte[] nameBytes = name.getBytes();
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final byte[] dataBytes = data.getBytes();

    final ByteBuffer byteBuffer = ByteBuffer.allocate(getLength());
    if (useCompression) {
      if (-1 == pointerShort) {
        throw new IllegalStateException(
            "The pointer has not been resolved yet.");
      }
      byteBuffer.putShort((short) (pointerShort | POINTER_MASK));
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
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return type.getLength() + recordClass.getLength() + TTL_LENGTH + LENGTH_RDATA_LENGTH +
        (useCompression ? POINTER_LENGTH : name.getLength()) + data.getLength();
  }

  /**
   * Gets the name of the resource record.
   *
   * @return the name of the resource record
   */
  public @NotNull DomainName name() {
    return name;
  }

  /**
   * Gets the type of the resource record.
   *
   * @return the type of the resource record
   */
  public @NotNull Type type() {
    return type;
  }

  /**
   * Gets the class of the resource record.
   *
   * @return the class of the resource record
   */
  public @NotNull DnsClass recordClass() {
    return recordClass;
  }

  /**
   * Gets the time to live of the resource record.
   *
   * @return the time to live of the resource record
   */
  public @Range(from = 0, to = Integer.MAX_VALUE) int ttl() {
    return ttl;
  }

  /**
   * Gets the data of the resource record.
   *
   * @return the data of the resource record
   */
  public @NotNull Rdata data() {
    return data;
  }

  /**
   * Whether the resource record is compressed.
   *
   * @return true if the resource record is compressed, false otherwise
   * @since 1.0.0
   */
  public boolean useCompression() {
    return useCompression;
  }

  /**
   * Gets the pointer to the name (if useCompression is true).
   *
   * @return the pointer to the name (if useCompression is true)
   * @since 1.0.0
   */
  @Nullable("if useCompression is false")
  public Writable pointer() {
    return pointer;
  }

  /**
   * Sets the pointer value.
   *
   * @param pointer pointer value
   * @since 1.0.0
   */
  public void pointerShort(final int pointer) {
    this.pointerShort = (short) pointer;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (null == obj || obj.getClass() != this.getClass()) {
      return false;
    }
    final var that = (ResourceRecord) obj;
    return this.useCompression == that.useCompression &&
        Objects.equals(this.pointer, that.pointer) &&
        Objects.equals(this.name, that.name) && this.type == that.type &&
        this.recordClass == that.recordClass && this.ttl == that.ttl &&
        Objects.equals(this.data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useCompression, pointer, name, type, recordClass, ttl, data);
  }

  @Override
  public String toString() {
    return "ResourceRecord["
        + "useCompression=" + useCompression + ", "
        + "pointer=" + pointer + ", "
        + "name=" + name + ", "
        + "type=" + type + ", "
        + "recordClass=" + recordClass + ", "
        + "ttl=" + ttl + ", "
        + "data=" + data + ", "
        + "pointerShort=" + pointerShort + ']';
  }

  /**
   * Builder for the {@link ResourceRecord resource record} entity.
   */
  public static class Builder {

    private boolean useCompression;
    private Writable pointer;
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
      if (resourceRecord.useCompression) {
        useCompression = true;
        pointer = resourceRecord.pointer;
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
     * @param pointer the pointer to the name of the resource record.
     * @return the builder
     * @since 1.0.0
     */
    public Builder pointer(final Writable pointer) {
      this.useCompression = true;
      this.pointer = pointer;
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
      if (null == pointer && useCompression) {
        throw new IllegalStateException(
            "Pointer is not set while compression is enabled");
      }
      if (null == name) {
        if (!useCompression) {
          throw new IllegalArgumentException(
              "name is not set while compression is disabled");
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
      if (useCompression) {
        return new ResourceRecord(pointer, type, recordClass, ttl, data);
      }
      return new ResourceRecord(name, type, recordClass, ttl, data);
    }
  }
}
