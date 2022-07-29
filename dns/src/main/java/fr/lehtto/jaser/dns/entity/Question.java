package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DNS question.
 *
 * @author Lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record Question(@NotNull DomainName name, @NotNull Type type,
                       @NotNull DnsClass recordClass) implements Writable {

  /**
   * Builder for the question.
   *
   * @return the builder
   */
  public static @NotNull Builder builder() {
    return new Builder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte @NotNull [] getBytes() {
    final byte[] nameBytes = name.getBytes();
    final byte[] typeBytes = type.getBytes();
    final byte[] recordClassBytes = recordClass.getBytes();
    final ByteBuffer byteBuffer = ByteBuffer.allocate(getLength());
    byteBuffer.put(nameBytes);
    byteBuffer.put(typeBytes);
    byteBuffer.put(recordClassBytes);
    return byteBuffer.array();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return name.getLength() + type.getLength() + recordClass.getLength();
  }

  /**
   * Builder for the question.
   *
   * @return the builder
   */
  public @NotNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * Builder for the question.
   */
  public static final class Builder {

    private @Nullable DomainName name;
    private @Nullable Type type;
    private @Nullable DnsClass recordClass;

    /**
     * Default constructor.
     */
    private Builder() {
      name = null;
      type = null;
      recordClass = null;
    }

    /**
     * Constructor with a question.
     *
     * @param question the question to copy
     */
    private Builder(final @NotNull Question question) {
      name = question.name;
      type = question.type;
      recordClass = question.recordClass;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     * @return the builder
     */
    public @NotNull Builder name(final @NotNull DomainName name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     * @return the builder
     */
    public @NotNull Builder type(final @NotNull Type type) {
      this.type = type;
      return this;
    }

    /**
     * Sets the record class.
     *
     * @param recordClass the record class
     * @return the builder
     */
    public @NotNull Builder recordClass(final @NotNull DnsClass recordClass) {
      this.recordClass = recordClass;
      return this;
    }

    /**
     * Builds the question.
     *
     * @return the question
     */
    public @NotNull Question build() {
      if (null == name) {
        throw new IllegalArgumentException("Name is not set");
      }
      if (null == type) {
        throw new IllegalArgumentException("Type is not set");
      }
      if (null == recordClass) {
        throw new IllegalArgumentException("Record class is not set");
      }
      return new Question(name, type, recordClass);
    }
  }
}
