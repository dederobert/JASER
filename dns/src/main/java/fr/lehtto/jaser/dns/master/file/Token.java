package fr.lehtto.jaser.dns.master.file;

import org.jetbrains.annotations.NotNull;

/**
 * A token is a piece of data that is separated by a separator.
 *
 * @author lehtto
 * @since 0.2.0
 */
record Token(@NotNull String value, @NotNull TokenType type) {

}
