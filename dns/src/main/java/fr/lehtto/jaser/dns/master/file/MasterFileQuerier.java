package fr.lehtto.jaser.dns.master.file;


import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * Querier for the master file.
 *
 * @author lehtto
 * @version 0.2.0
 */
public class MasterFileQuerier {

  private final List<MasterFile> masterFiles;

  /**
   * Valued constructor.
   *
   * @param masterFiles the master files to query from
   */
  public MasterFileQuerier(final @NotNull List<MasterFile> masterFiles) {
    this.masterFiles = masterFiles;
  }

  /**
   * Filters the master files by the given class.
   *
   * @param dnsClass the class to filter by
   * @return the filtered master files
   */
  public ClassFilteredMasterFileQuerier withClass(final @NotNull DnsClass dnsClass) {
    final Stream<MasterFile> filteredMasterFiles = this.masterFiles.stream()
        .filter(masterFile -> masterFile.getDnsClass() == dnsClass);
    return new ClassFilteredMasterFileQuerier(filteredMasterFiles);
  }

  /**
   * Querier with filtered master files.
   */
  @SuppressWarnings("WeakerAccess") // This class is used
  public record ClassFilteredMasterFileQuerier(@NotNull Stream<MasterFile> masterFiles) {

    /**
     * Filters the records by the given type.
     *
     * @param type the type to filter by
     * @return the filtered records
     */
    public @NotNull TypeFilteredMasterFileQuerier withType(final @NotNull Type type) {
      final Stream<ResourceRecord> filteredRecords = this.masterFiles
          .map(MasterFile::getRecords)
          .flatMap(List::stream)
          .filter(resourceRecord -> resourceRecord.type() == type);
      return new TypeFilteredMasterFileQuerier(filteredRecords);
    }
  }

  /**
   * Querier with filtered records.
   */
  public record TypeFilteredMasterFileQuerier(@NotNull Stream<ResourceRecord> records) {

    /**
     * Filters the records by the given name.
     *
     * @param domain the name to filter by
     * @return the filtered records
     */
    @NotNull
    public TypeFilteredMasterFileQuerier withDomain(final @NotNull String domain) {
      final Stream<ResourceRecord> filteredRecords = this.records
          .filter(resourceRecord -> domainMatch(resourceRecord.name(), domain));
      return new TypeFilteredMasterFileQuerier(filteredRecords);
    }

    /**
     * Gets stream of matching records.
     *
     * @return the stream of matching records
     */
    @NotNull
    public Stream<ResourceRecord> getRecordsStream() {
      return records;
    }

    /**
     * Checks if the given name matches the given domain.
     *
     * @param domain the domain to check
     * @param name   the name to check
     * @return true if the given name matches the given domain, false otherwise
     */
    private boolean domainMatch(final @NotNull DomainName domain, final @NotNull String name) {

      final String transformedName = name
          .replace((char) 0x7, (char) 0x3)
          .replace('.', (char) 0x3)
          .trim();

      final String transformedNameToCompare = domain
          .value()
          .replace((char) 0x7, (char) 0x3)
          .replace('.', (char) 0x3)
          .trim();

      return transformedNameToCompare.equals(transformedName);
    }
  }
}
