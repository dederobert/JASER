package fr.lehtto.jaser.dns.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Writable interface.
 *
 * @author Lehtto
 * @since 0.1.0
 */
@FunctionalInterface
public interface Writable {

  /**
   * Gets array of bytes representing the entity.
   *
   * @return the array of bytes representing the entity
   */
  byte @NotNull [] getBytes();

}
