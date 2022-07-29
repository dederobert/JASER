package fr.lehtto.jaser.dns.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Writable interface.
 *
 * @author Lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Writable {

  /**
   * Gets array of bytes representing the entity.
   *
   * @return the array of bytes representing the entity
   */
  byte @NotNull [] getBytes();

  /**
   * Gets the length of the entity in bytes.
   *
   * @return the length of the entity in bytes
   * @since 1.0.0
   */
  int getLength();

}
