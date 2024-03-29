package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.MultiNamedRData;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MINFO RDATA entity (RFC 1035, section 3.3.7) (Experimental).
 *
 * @author lehtto
 * @version 1.0.0
 * @since 0.1.0
 */
public record MinfoRdata(@NotNull DomainName rmailbx, @NotNull DomainName emailbx) implements Rdata, MultiNamedRData {

  @Override
  public byte @NotNull [] getBytes() {
    final byte[] rmailbxBytes = rmailbx.getBytes();
    final byte[] emailbxBytes = emailbx.getBytes();
    final ByteBuffer buffer = ByteBuffer.allocate(rmailbxBytes.length + emailbxBytes.length);
    buffer.put(rmailbxBytes);
    buffer.put(emailbxBytes);
    return buffer.array();
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.0.0
   */
  @Override
  public int getLength() {
    return rmailbx.getLength() + emailbx.getLength();
  }

  @Override
  public @NotNull DomainName[] getNames() {
    return new DomainName[]{rmailbx, emailbx};
  }

  /**
   * Parses the given string into a MINFO RDATA.
   */
  public static class MinfoRdataParser extends RDataParser {

    /**
     * Valued constructor.
     *
     * @param next the next parser to use
     */
    public MinfoRdataParser(final @Nullable RDataParser next) {
      super(next);
    }

    @Override
    protected @Nullable Rdata handle(final @NotNull Type type, final @NotNull String @NotNull [] parts) {
      return null;
    }
  }
}
