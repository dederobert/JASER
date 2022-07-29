package fr.lehtto.jaser.dns.entity.rdata;

import fr.lehtto.jaser.dns.entity.DomainName;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for all {@link Rdata} that have one or more names.
 */
@FunctionalInterface
public interface MultiNamedRData {

  /**
   * Gets the names of this {@link Rdata}.
   *
   * @return the list of names
   */
  @NotNull DomainName[] getNames();
}
