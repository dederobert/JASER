package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  private final @NotNull List<Zone> zones = new ArrayList<>();
  private DnsClass dnsClass;

  /**
   * Adds a record to the master file.
   *
   * @param resourceRecord the resource record to add
   * @throws InvalidDnsZoneEntryException if the record is invalid
   */
  public void addRecord(final @NotNull ResourceRecord resourceRecord)
      throws InvalidDnsZoneEntryException {
    // Check if the record is valid.
    if (null == dnsClass) {
      dnsClass = resourceRecord.recordClass();
    } else if (dnsClass != resourceRecord.recordClass()) {
      // The record class is not the same as the master file's class.
      throw new InvalidDnsZoneEntryException(
          "The record class is not the same as the master file's class.");
    }

    final String[] labels = resourceRecord.name().labels();
    final int length = labels.length;
    insertResourceRecord(resourceRecord, labels, length - 1);
  }

  /**
   * Gets the zone's size.
   *
   * @return the zone's size
   */
  public int size() {
    return zones.stream().mapToInt(Zone::size).sum();
  }

  /**
   * Gets the zone's records.
   *
   * @return the zone's records
   * @deprecated use {@link #zones} instead
   */
  @UnmodifiableView
  @Deprecated(forRemoval = true)
  public @NotNull List<ResourceRecord> getRecords() {
    return zones.stream()
        .map(Zone::getRecordsFlatList)
        .flatMap(List::stream)
        .toList();
  }

  /**
   * Gets the zone's class.
   *
   * @return the zone's class
   */
  public DnsClass getDnsClass() {
    return dnsClass;
  }

  /**
   * Inserts a resource record in the master file.
   *
   * @param resourceRecord the resource record to insert
   * @param labels         the labels of the resource record
   * @param labelPosition  the position of the label to insert the resource record in
   */
  private void insertResourceRecord(final @NotNull ResourceRecord resourceRecord,
      final String[] labels,
      final int labelPosition) {
    final @NotNull List<Zone> children = this.zones;

    final Zone newZone = insertResourceRecord(children, resourceRecord, labels, labelPosition);
    if (null != newZone) {
      this.zones.add(newZone);
    }
  }

  /**
   * Inserts a resource record in the master file.
   *
   * @param parent         the parent zone
   * @param resourceRecord the resource record to insert
   * @param labels         the labels of the resource record
   * @param labelPosition  the position of the label to insert the resource record in
   */
  private void insertResourceRecord(final @NotNull Zone parent,
      final @NotNull ResourceRecord resourceRecord,
      final String[] labels,
      final int labelPosition) {
    final @NotNull List<Zone> children = parent.getSubZones();
    final Zone newZone = insertResourceRecord(children, resourceRecord, labels, labelPosition);
    if (null != newZone) {
      final List<Zone> newChildren = new ArrayList<>(children);
      newChildren.add(newZone);
      parent.setSubZones(newChildren);
    }
  }


  /**
   * Inserts a resource record in the master file.
   *
   * @param children       the children zones
   * @param resourceRecord the resource record to insert
   * @param labels         the labels of the resource record
   * @param labelPosition  the position of the label to insert the resource
   * @return the new zone or null if zone already exists
   */
  @Nullable
  private Zone insertResourceRecord(final @NotNull List<Zone> children,
      final
      @NotNull ResourceRecord resourceRecord,
      final String[] labels,
      final int labelPosition) {
    for (final Zone zone : children) {
      if (labels[labelPosition].equals(zone.getLabel())) {
        if (0 == labelPosition) {
          zone.addRecord(resourceRecord);
          return null;
        }
        insertResourceRecord(zone, resourceRecord, labels, labelPosition - 1);
        return null;
      }
    }
    final Zone newZone = new Zone(labels[labelPosition]);
    if (0 == labelPosition) {
      newZone.addRecord(resourceRecord);
    } else {
      insertResourceRecord(newZone, resourceRecord, labels, labelPosition - 1);
    }
    return newZone;
  }


  /**
   * Searches for a resource record in the master file.
   *
   * @param domainName the domain name to search for
   * @return the resource record if found
   */
  public Optional<Zone> search(final @NotNull DomainName domainName) {
    final String[] labels = domainName.labels();
    for (final Zone zone : zones) {
      final Optional<Zone> optionalZone =
          zone.search(labels, labels.length - 1);
      if (optionalZone.isPresent()) {
        return optionalZone;
      }
    }
    return Optional.empty();
  }
}
