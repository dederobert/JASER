package fr.lehtto.jaser.dns.entity.rdata;

import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.internet.ARdata.ARdataParser;
import fr.lehtto.jaser.dns.entity.rdata.internet.AaaRdata.AaaRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.internet.WksRdata.WksRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.CnameRdata.CnameParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.HinfoRdata.HinfoParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MbRdata.MbRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MdRdata.MdRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MfRdata.MfRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MgRdata.MgRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MinfoRdata.MinfoRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MrRdata.MrRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.MxRdata.MxRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.NsRdata.NsRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.NullRdata.NullRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.PtrRdata.PtrRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.SoaRdata.SoaRdataParser;
import fr.lehtto.jaser.dns.entity.rdata.standard.TxtRdata.TxtRdataParser;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for RDATA.
 *
 * @author Lehtto
 * @since 0.1.0
 */
public final class RDataFactory {

  private static final @NotNull RDataParser PARSER;

  static {
    RDataParser parser = new CnameParser(null);
    parser = new AaaRdataParser(parser);
    parser = new HinfoParser(parser);
    parser = new MbRdataParser(parser);
    parser = new MdRdataParser(parser);
    parser = new MfRdataParser(parser);
    parser = new MgRdataParser(parser);
    parser = new MinfoRdataParser(parser);
    parser = new MrRdataParser(parser);
    parser = new MxRdataParser(parser);
    parser = new NsRdataParser(parser);
    parser = new NullRdataParser(parser);
    parser = new PtrRdataParser(parser);
    parser = new SoaRdataParser(parser);
    parser = new TxtRdataParser(parser);
    parser = new WksRdataParser(parser);
    PARSER = new ARdataParser(parser);
  }

  /**
   * Default constructor.
   */
  private RDataFactory() {
    throw new AssertionError("This constructor should not be called.");
  }

  /**
   * Create a RDATA entity from a type and a byte array.
   *
   * @param type the type of the RDATA entity.
   * @param data the byte array of the RDATA entity.
   * @return the RDATA entity.
   * @throws InvalidDnsZoneEntryException if the RDATA entity is invalid.
   */
  public static Rdata create(final @NotNull Type type, final @NotNull String @NotNull [] data)
      throws InvalidDnsZoneEntryException {
    return PARSER.parse(type, data);
  }
}
