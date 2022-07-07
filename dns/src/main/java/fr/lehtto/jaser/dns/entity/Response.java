package fr.lehtto.jaser.dns.entity;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * DNS response entity.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public record Response(@NotNull Header header,
                       @NotNull List<Question> questions,
                       @NotNull List<ResourceRecord> answerRecords,
                       @NotNull List<ResourceRecord> authorityRecords,
                       @NotNull List<ResourceRecord> additionalRecords) {

  /**
   * Creates a new builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a new builder.
   *
   * @return the builder
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * Builder for {@link Response}.
   */
  public static class Builder {

    private Header header;
    private List<Question> questions;
    private List<ResourceRecord> answers;
    private List<ResourceRecord> authorityRecords;
    private List<ResourceRecord> additionalRecords;

    /**
     * Default constructor.
     */
    Builder() {
      header = null;
      questions = null;
      answers = null;
      authorityRecords = null;
      additionalRecords = null;
    }

    /**
     * Copy constructor.
     *
     * @param response the response to copy
     */
    Builder(final @NotNull Response response) {
      header = response.header;
      questions = response.questions;
      answers = response.answerRecords;
      authorityRecords = response.authorityRecords;
      additionalRecords = response.additionalRecords;
    }

    /**
     * Sets the header of the response.
     *
     * @param header the header to set
     * @return this builder
     */
    public Builder header(final @NotNull Header header) {
      this.header = header;
      return this;
    }

    /**
     * Sets the questions of the response.
     *
     * @param question the question to set
     * @return this builder
     */
    public Builder question(final @NotNull Question question) {
      return questions(List.of(question));
    }

    /**
     * Sets the questions of the response.
     *
     * @param questions the questions to set
     * @return this builder
     */
    public Builder questions(final @NotNull List<Question> questions) {
      this.questions = questions;
      return this;
    }

    /**
     * Sets the answer records of the response.
     *
     * @param answerRecords the answer records to set
     * @return this builder
     */
    public Builder answers(final @NotNull List<ResourceRecord> answerRecords) {
      this.answers = answerRecords;
      return this;
    }

    /**
     * Sets no answer records of the response.
     *
     * @return this builder
     */
    public Builder noAnswer() {
      this.answers = List.of();
      return this;
    }

    /**
     * Sets the authority records of the response.
     *
     * @param authorityRecords the authority records to set
     * @return this builder
     */
    public Builder authorityRecords(final @NotNull List<ResourceRecord> authorityRecords) {
      this.authorityRecords = authorityRecords;
      return this;
    }

    /**
     * Sets no authority records of the response.
     *
     * @return this builder
     */
    public Builder noAuthorityRecords() {
      this.authorityRecords = List.of();
      return this;
    }

    /**
     * Sets the additional records of the response.
     *
     * @param additionalRecords the additional records to set
     * @return this builder
     */
    public Builder additionalRecords(final @NotNull List<ResourceRecord> additionalRecords) {
      this.additionalRecords = additionalRecords;
      return this;
    }

    /**
     * Sets no additional records of the response.
     *
     * @return this builder
     */
    public Builder noAdditionalRecords() {
      this.additionalRecords = List.of();
      return this;
    }

    /**
     * Builds the response.
     *
     * @return the response
     */
    public Response build() {
      if (null == header) {
        throw new IllegalStateException("Header is not set");
      }
      if (null == questions) {
        throw new IllegalStateException("Questions are not set");
      }
      if (null == answers) {
        throw new IllegalStateException("Answer records are not set");
      }
      if (null == authorityRecords) {
        throw new IllegalStateException("Authority records are not set");
      }
      if (null == additionalRecords) {
        throw new IllegalStateException("Additional records are not set");
      }
      return new Response(header, questions, answers, authorityRecords, additionalRecords);
    }
  }
}
