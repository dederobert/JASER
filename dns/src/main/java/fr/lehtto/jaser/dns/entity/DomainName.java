package fr.lehtto.jaser.dns.entity;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;

/**
 * A domain name is a sequence of labels, each label is a sequence of characters.
 *
 * @author lehtto
 * @since 0.2.0
 */
@SuppressWarnings("NumericCastThatLosesPrecision")
public final class DomainName {

  private final String @NotNull [] labels;
  private final @NotNull String value;

  /**
   * Valued constructor.
   *
   * @param labels the domain name labels
   * @param value  the domain name value
   */
  private DomainName(final String @NotNull [] labels, @NotNull final String value) {
    this.labels = labels;
    this.value = value;
  }

  /**
   * Creates domain name from a string.
   *
   * @param name the string
   * @return the domain name
   */
  public static DomainName of(final @NotNull String name) {
    return new DomainName(name.split("[.]"), name);
  }

  /**
   * Creates domain name from an array of labels.
   *
   * @param labels the labels
   * @return the domain name
   */
  public static DomainName of(final String @NotNull [] labels) {
    final StringJoiner joiner = new StringJoiner(".");
    for (final String label : labels) {
      joiner.add(label);
    }
    return new DomainName(labels, joiner.toString());
  }

  /**
   * Gets the value of this domain name as a byte array.
   *
   * @return the domain name as a byte array.
   */
  public byte @NotNull [] toBytes() {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(value.length() + 2);
    for (final String label : labels) {
      byteBuffer.put((byte) label.length());
      byteBuffer.put(label.getBytes(StandardCharsets.UTF_8));
    }
    byteBuffer.put((byte) 0);
    return byteBuffer.array();
  }

  /**
   * Gets the domain name as a string.
   *
   * @return the domain name as a string.
   */
  public String value() {
    return value;
  }

  /**
   * Gets the domain name as an array of labels.
   *
   * @return the domain name as an array of labels.
   */
  public String @NotNull [] labels() {
    return labels;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final DomainName that = (DomainName) o;
    return Arrays.equals(labels, that.labels);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(labels);
  }

  @Override
  public String toString() {
    return "DomainName{" +
        "labels=" + Arrays.toString(labels) +
        ", value='" + value + '\'' +
        '}';
  }
}
