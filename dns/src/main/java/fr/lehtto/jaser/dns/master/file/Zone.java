package fr.lehtto.jaser.dns.master.file;

import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Zone DNS.
 *
 * @author Lehtto
 * @version 0.3.0
 */
public class Zone {

  private final @NotNull String label;
  private final @NotNull List<ResourceRecord> records = new ArrayList<>();
  private final @NotNull List<Zone> subZones = new ArrayList<>();


  /**
   * Valued constructor.
   *
   * @param label the label of the zone
   */
  Zone(final @NotNull String label) {
    this.label = label;
  }

  /**
   * Gets the label of the zone.
   *
   * @return the label of the zone
   */
  @NotNull String getLabel() {
    return label;
  }

  /**
   * Gets flat list of all records.
   *
   * @return the records
   */
  @Unmodifiable
  @NotNull List<ResourceRecord> getRecordsFlatList() {
    final List<ResourceRecord> result = new ArrayList<>(records);
    for (final Zone zone : subZones) {
      result.addAll(zone.getRecordsFlatList());
    }
    return List.copyOf(result);
  }

  /**
   * Gets the records of the zone.
   *
   * @return the records
   */
  @Unmodifiable
  public @NotNull List<ResourceRecord> getRecords() {
    return List.copyOf(records);
  }

  /**
   * Gets the sub zones of the zone.
   *
   * @return the subZones
   */
  @Unmodifiable
  @NotNull List<Zone> getSubZones() {
    return List.copyOf(subZones);
  }

  /**
   * Sets the sub zones of the zone.
   *
   * @param subZones the new sub zones
   */
  void setSubZones(final @Nullable List<Zone> subZones) {
    this.subZones.clear();
    if (null != subZones) {
      this.subZones.addAll(subZones);
    }
  }

  /**
   * Adds a record to the zone.
   *
   * @param dnsRecord the record to add
   */
  void addRecord(final @NotNull ResourceRecord dnsRecord) {
    records.add(dnsRecord);
  }

  /**
   * Adds a sub zone to the zone.
   *
   * @param zone the zone to add
   */
  public void addSubZone(final @Nullable Zone zone) {
    if (null != zone) {
      subZones.add(zone);
    }
  }

  /**
   * Checks whether the zone contains a SOA record.
   *
   * @return true if the zone contains a SOA record, false otherwise
   */
  private boolean isSoaPresent() {
    return records.stream().anyMatch(r -> Type.SOA == r.type()) || subZones.stream().anyMatch(Zone::isSoaPresent);
  }

  /**
   * Gets the size of the zone.
   *
   * @return the size of the zone
   */
  public int size() {
    return records.size() + subZones.stream().mapToInt(Zone::size).sum();
  }

  /**
   * Searches for a zone with the given label.
   *
   * @param labels   the labels
   * @param position the label position
   * @return the zone
   */
  Optional<Zone> search(final String[] labels, final int position) {
    if (labels[position].equals(label)) {
      if (0 == position) {
        return Optional.of(this);
      }
      for (final Zone zone : subZones) {
        final Optional<Zone> optionalZone = zone.search(labels, position - 1);
        if (optionalZone.isPresent()) {
          return optionalZone;
        }
      }
    }
    return Optional.empty();
  }
}
