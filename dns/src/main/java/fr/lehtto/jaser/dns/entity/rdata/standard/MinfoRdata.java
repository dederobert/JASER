package fr.lehtto.jaser.dns.entity.rdata.standard;

import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.rdata.RDataParser;
import fr.lehtto.jaser.dns.entity.rdata.Rdata;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MINFO RDATA entity (RFC 1035, section 3.3.7) (Experimental).
 *
 * @author lehtto
 * @version 0.1.0
 */
public record MinfoRdata(@NotNull DomainName rmailbx, @NotNull DomainName emailbx) implements Rdata {

  @Override
  public byte @NotNull [] getBytes() {
    final byte[] rmailbxBytes = rmailbx.toBytes();
    final byte[] emailbxBytes = emailbx.toBytes();
    final ByteBuffer buffer = ByteBuffer.allocate(rmailbxBytes.length + emailbxBytes.length);
    buffer.put(rmailbxBytes);
    buffer.put(emailbxBytes);
    return buffer.array();
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
