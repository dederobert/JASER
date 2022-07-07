package fr.lehtto.jaser.dns.entity;

import fr.lehtto.jaser.core.utils.StringUtils;
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

  /**
   * Checks if this question name is equal to the given name.
   * <p/>
   * Removes '.' and ' ' from both names before comparing.
   * <p/>
   * <b>Note:</b> this method is case-sensitive.
   *
   * @param name the name to compare to
   * @return true if this question name is equal to the given name, false otherwise
   */
  public boolean nameMatch(final @NotNull String name) {
    if (StringUtils.isEmpty(name)) {
      return false;
    }
    final String transformedName = this.name.replace('.', (char) 0x3).trim();
    final String transformedNameToCompare = name.replace('.', (char) 0x3).trim();

    return transformedNameToCompare.equals(transformedName);
  }
}
