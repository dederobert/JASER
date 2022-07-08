package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Context for the parser.
 *
 * @author Lehtto
 * @version 0.2.0
 */
class ParserInputContext {

  private final List<String> lines = new ArrayList<>();
  private int lineIndex = -1;

  // region stated fields
  private @NotNull String domain;
  private Integer ttl;
  private DnsClass dnsClass;

  // endregion

  /**
   * Valued constructor.
   *
   * @param lines the lines to parse
   * @param domain the zone to parse
   */
  ParserInputContext(final List<String> lines, final @NotNull String domain) {
    setLines(lines);
    this.domain = domain;
  }


  /**
   * Sets the lines to parse.
   *
   * @param lines the lines to parse
   */
  private void setLines(final @Nullable List<String> lines) {
    this.lines.clear();
    if (null != lines) {
      this.lines.addAll(lines);
    }
  }

  /**
   * Gets the stated domain.
   *
   * @return the stated domain
   */
  @NotNull String getDomain() {
    return domain;
  }

  /**
   * Sets the stated domain.
   *
   * @param domain the domain to set
   */
  void setDomain(final @NotNull String domain) {
    this.domain = domain;
  }

  /**
   * Gets the stated TTL.
   *
   * @return the stated TTL
   */
  @Nullable Integer getTtl() {
    return ttl;
  }

  /**
   * Sets the stated TTL.
   *
   * @param ttl the TTL to set
   */
  void setTtl(final Integer ttl) {
    this.ttl = ttl;
  }

  /**
   * Gets the stated dns class.
   *
   * @return the stated dns class
   */
  @Nullable DnsClass getDnsClass() {
    return dnsClass;
  }

  /**
   * Sets the stated DNS class.
   *
   * @param dnsClass the DNS class to set
   */
  void setDnsClass(final DnsClass dnsClass) {
    this.dnsClass = dnsClass;
  }

  /**
   * Gets the next line to parse.
   *
   * @return the next line to parse
   */
  String nextLine() {
    ++lineIndex;
    return lines.get(lineIndex);
  }

  /**
   * Checks if the current line is the last one.
   *
   * @return true if the current line is the last one, false otherwise
   */
  boolean hasNextLine() {
    return lineIndex + 1 < lines.size();
  }

  /**
   * Gets the current line index.
   *
   * @return the current line index
   */
  int getLineIndex() {
    return lineIndex;
  }

  /**
   * Adds lines to the context.
   *
   * @param includedLines the lines to add
   */
  void addLines(final List<String> includedLines) {
    lines.addAll(lineIndex + 1, includedLines);
  }
}
