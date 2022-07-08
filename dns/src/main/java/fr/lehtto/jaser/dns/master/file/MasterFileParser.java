package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.RDataFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for DNS master file.
 *
 * @author lehtto
 * @version 0.2.0
 */
public final class MasterFileParser {

  private static final Logger LOG = LogManager.getLogger(MasterFileParser.class);

  /**
   * Default constructor.
   */
  private MasterFileParser() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Parses a DNS master file.
   *
   * @param masterFile the master file to parse
   * @return the parsed master file
   * @throws InvalidDnsZoneEntryException if the master file is invalid
   */
  public static @NotNull MasterFile parse(final File masterFile) throws InvalidDnsZoneEntryException {
    // Determine the zone name from the file name.
    final String domain;
    if (masterFile.getName().endsWith(".zone")) {
      domain = masterFile.getName().substring(0, masterFile.getName().length() - 5);
    } else {
      LOG.warn("Please consider using a .zone file.");
      domain = masterFile.getName();
    }

    // Read the file.
    final List<String> lines = readLines(masterFile);
    // Create the master file.
    final MasterFile master = new MasterFile();

    // Parse the file.
    final ParserInputContext parserInputContext = new ParserInputContext(lines, domain);
    while (parserInputContext.hasNextLine()) {
      final String line = parserInputContext.nextLine();
      if (line.isEmpty()) {
        continue;
      }

      // Tokenize the line.
      final List<Token> tokens = LineParser.tokenize(line);
      // Parse tokens.
      final List<ParsedToken> parsedTokens = LineParser.parseToken(tokens);

      if (ParseTokenStateMachine.COMMENT == parsedTokens.get(0).type()) {
        // This is a comment line.
        continue;
      }

      if (ParseTokenStateMachine.CONTROL == parsedTokens.get(0).type()) {
        // This is a control line.
        handleControlLine(masterFile, parsedTokens, parserInputContext);
        continue;
      }

      // This is a resource record line.
      final String recordDomain = retrieveDomain(parserInputContext, parsedTokens);
      final Type type = retrieveType(parsedTokens);
      final int ttl = retrieveTtl(parserInputContext, parsedTokens);
      final DnsClass dnsClass = retrieveClass(parserInputContext, parsedTokens);
      final String[] data = retrieveData(parsedTokens);

      final ResourceRecord resourceRecord = ResourceRecord.builder()
          .name(recordDomain)
          .type(type)
          .ttl(ttl)
          .recordClass(dnsClass)
          .data(RDataFactory.create(type, data))
          .build();

      master.addRecord(resourceRecord);
    }
    return master;
  }

  /**
   * Handles a control line.
   *
   * @param masterFile         the master file to parse
   * @param parsedTokens       the parsed tokens
   * @param parserInputContext the parser input context
   * @throws InvalidDnsZoneEntryException if the master file is invalid
   */
  private static void handleControlLine(final @NotNull File masterFile,
      final @NotNull List<ParsedToken> parsedTokens, final @NotNull ParserInputContext parserInputContext)
      throws InvalidDnsZoneEntryException {
    LOG.debug("Control line: {}", parsedTokens);
    switch (parsedTokens.get(0).value()) {
      case "$INCLUDE" -> {
        final String fileName = parsedTokens.get(1).value();
        final File file = new File(masterFile.getParent(), fileName);
        final List<String> includedLines = readLines(file);
        parserInputContext.addLines(includedLines);
        LOG.debug("Included file: {}", file);
      }
      case "$ORIGIN" -> {
        final String origin = parsedTokens.get(1).value();
        parserInputContext.setDomain(origin);
        LOG.debug("Origin: {}", origin);
      }
      case "$TTL" -> {
        final int ttl = Integer.parseInt(parsedTokens.get(1).value());
        parserInputContext.setTtl(ttl);
        LOG.debug("TTL: {}", ttl);
      }
      case "$CLASS" -> {
        final DnsClass dnsClass = DnsClass.valueOf(parsedTokens.get(1).value());
        parserInputContext.setDnsClass(dnsClass);
        LOG.debug("Class: {}", dnsClass);
      }
      default -> throw new InvalidDnsZoneEntryException("Unknown control line: " + parsedTokens);
    }
  }

  /**
   * Reads the lines of a file.
   *
   * @param file the file to read
   * @return the lines of the file
   */
  private static List<String> readLines(final @NotNull File file) {
    // Read the file.
    final List<String> lines = new ArrayList<>();
    try (final Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
      while (scanner.hasNextLine()) {
        final String line = scanner.nextLine();
        lines.add(line);
      }
    } catch (final IOException e) {
      LOG.warn("Could not read file.", e);
    }
    return lines;
  }

  /**
   * Retrieves the domain from the parsed tokens or stated domain.
   *
   * @param parserInputContext the parser input context
   * @param parsedTokens       the parsed tokens
   * @return the domain
   */
  private static @NotNull String retrieveDomain(final @NotNull ParserInputContext parserInputContext,
      final @NotNull List<ParsedToken> parsedTokens) {

    final String domain = parsedTokens.stream()
        .filter(token -> ParseTokenStateMachine.DOMAIN == token.type())
        .map(ParsedToken::value)
        .findFirst().orElse("");

    // If domain ends with a dot, it is absolute.
    if (domain.endsWith(".")) {
      return domain;
    }

    if (domain.isEmpty()) {
      return parserInputContext.getDomain();
    }
    return domain + '.' + parserInputContext.getDomain();
  }

  /**
   * Retrieves the type from the parsed tokens.
   *
   * @param parsedTokens the parsed tokens
   * @return the type
   * @throws InvalidDnsZoneEntryException if the type is not found
   */
  private static @NotNull Type retrieveType(final @NotNull List<ParsedToken> parsedTokens)
      throws InvalidDnsZoneEntryException {
    return parsedTokens.stream()
        .filter(token -> ParseTokenStateMachine.TYPE == token.type())
        .map(ParsedToken::value)
        .findFirst()
        .map(Type::valueOf)
        .orElseThrow(() -> new InvalidDnsZoneEntryException("Could not determine type."));
  }

  /**
   * Retrieves the TTL from the parsed tokens or stated TTL.
   *
   * @param parserInputContext the parser input context
   * @param parsedTokens       the parsed tokens
   * @return the TTL
   */
  private static int retrieveTtl(final @NotNull ParserInputContext parserInputContext,
      final @NotNull List<ParsedToken> parsedTokens) {
    final Integer ttl = parsedTokens.stream()
        .filter(token -> ParseTokenStateMachine.TTL == token.type())
        .map(ParsedToken::value)
        .findFirst()
        .map(Integer::parseInt)
        .orElseGet(parserInputContext::getTtl);
    assert null != ttl : "TTL should not be null.";
    return ttl;
  }

  /**
   * Retrieves the DNS class from the parsed tokens or stated DNS class.
   *
   * @param parserInputContext the parser input context
   * @param parsedTokens       the parsed tokens
   * @return the DNS class
   */
  private static @NotNull DnsClass retrieveClass(final @NotNull ParserInputContext parserInputContext,
      final @NotNull List<ParsedToken> parsedTokens) {
    final DnsClass dnsClass = parsedTokens.stream()
        .filter(token -> ParseTokenStateMachine.CLASS == token.type())
        .map(ParsedToken::value)
        .findFirst()
        .map(DnsClass::valueOf)
        .orElseGet(parserInputContext::getDnsClass);
    assert null != dnsClass : "DnsClass should not be null.";
    return dnsClass;
  }

  /**
   * Retrieves the data from the parsed tokens.
   *
   * @param parsedTokens the parsed tokens
   * @return the data
   */
  private static String @NotNull [] retrieveData(final @NotNull List<ParsedToken> parsedTokens) {
    return parsedTokens.stream()
        .filter(token -> ParseTokenStateMachine.DATA == token.type())
        .map(ParsedToken::value)
        .toArray(String[]::new);
  }
}
