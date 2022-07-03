package fr.lehtto.jaser.dns.entity;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * DNS response entity.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Response(@NotNull Header header,
                       @NotNull Set<Question> questions,
                       @NotNull Set<Answer> answerRecords,
                       @NotNull Set<Authority> authorityRecords,
                       @NotNull Set<Additional> additionalRecords) {

}
