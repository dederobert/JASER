package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * DNS master file.
 *
 * @author lehtto
 * @version 0.2.0
 */
public class MasterFile {

  private final List<ResourceRecord> records = new ArrayList<>();
  private DnsClass dnsClass;


  /**
   * Adds a record to the master file.
   *
   * @param resourceRecord the resource record to add
   * @throws InvalidDnsZoneEntryException if the record is invalid
   */
  void addRecord(final @NotNull ResourceRecord resourceRecord) throws InvalidDnsZoneEntryException {
    // Check if the record is valid.
    if (null == dnsClass) {
      dnsClass = resourceRecord.recordClass();
    } else if (dnsClass != resourceRecord.recordClass()) {
      // The record class is not the same as the master file's class.
      throw new InvalidDnsZoneEntryException("The record class is not the same as the master file's class.");
    }

    if (Type.SOA == resourceRecord.type() && isSoaPresent()) {
      // The SOA record is already present.
      throw new InvalidDnsZoneEntryException("The SOA record is already present.");
    }

    records.add(resourceRecord);
  }

  /**
   * Checks if SOA record is already present.
   *
   * @return true if SOA record is already present, false otherwise
   */
  private boolean isSoaPresent() {
    // Check if the record is valid.
    return records.stream().map(ResourceRecord::type).anyMatch(Predicate.isEqual(Type.SOA));
  }

  /**
   * Gets the zone's size.
   *
   * @return the zone's size
   */
  public int size() {
    return records.size();
  }

  /**
   * Gets the zone's records.
   *
   * @return the zone's records
   */
  @UnmodifiableView
  public @NotNull List<ResourceRecord> getRecords() {
    return List.copyOf(records);
  }

  /**
   * Sets the zone's records.
   *
   * @param resourceRecords the zone's records
   */
  public void setResourceRecords(final @Nullable List<ResourceRecord> resourceRecords) {
    records.clear();
    if (null != resourceRecords) {
      records.addAll(resourceRecords);
    }
  }
}
