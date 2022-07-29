package fr.lehtto.jaser.dns.entity.rdata;

import fr.lehtto.jaser.dns.entity.DomainName;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for all named {@link Rdata}.
 */
@FunctionalInterface
public interface NamedRData {

  /**
   * Gets the name of the {@link Rdata}.
   *
   * @return the name of the {@link Rdata}
   */
  @NotNull DomainName getName();
}
