package fr.lehtto.jaser.dns.entity.rdata;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * RDATA factory.
 *
 * @author lehtto
 * @since 0.1.0
 */
public abstract class RDataParser {

  private final @Nullable RDataParser next;

  /**
   * Valued constructor.
   *
   * @param next the next parser to use
   */
  protected RDataParser(final @Nullable RDataParser next) {
    this.next = next;
  }

  /**
   * Handle a type and a string array.
   *
   * @param type  the type of the RDATA entity.
   * @param parts the parts of the RDATA
   * @return the RDATA, or null if not found
   * @throws InvalidDnsZoneEntryException if the RDATA is invalid
   */
  protected abstract @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts)
      throws InvalidDnsZoneEntryException;

  /**
   * Parses the given string into a RDATA.
   *
   * @param type  the type of the RDATA entity.
   * @param parts The parts of the RDATA.
   * @return The RDATA, or null if the given string is not a valid RDATA.
   * @throws InvalidDnsZoneEntryException if the given string is not a valid RDATA.
   */
  Rdata parse(final @NotNull Type type, final @NotNull String @NotNull [] parts) throws InvalidDnsZoneEntryException {
    final Rdata rdata = handle(type, parts);
    if (null == rdata) {
      if (null != next) {
        return next.parse(type, parts);
      }
      throw new InvalidDnsZoneEntryException("Unable to parse RDATA with type " + type);
    }
    return rdata;
  }
}
