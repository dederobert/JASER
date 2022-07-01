package fr.lehtto.jaser.dns.entity;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * DNS response.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Response(@NotNull Header header, @NotNull Set<Question> questions, @NotNull Set<Answer> answers) {

}
