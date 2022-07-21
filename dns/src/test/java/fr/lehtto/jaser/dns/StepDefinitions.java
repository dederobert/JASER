package fr.lehtto.jaser.dns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.lehtto.jaser.dns.entity.AddressV4;
import fr.lehtto.jaser.dns.entity.DomainName;
import fr.lehtto.jaser.dns.entity.Flags;
import fr.lehtto.jaser.dns.entity.Header;
import fr.lehtto.jaser.dns.entity.Query;
import fr.lehtto.jaser.dns.entity.Question;
import fr.lehtto.jaser.dns.entity.ResourceRecord;
import fr.lehtto.jaser.dns.entity.Response;
import fr.lehtto.jaser.dns.entity.enumration.DnsClass;
import fr.lehtto.jaser.dns.entity.enumration.OpCode;
import fr.lehtto.jaser.dns.entity.enumration.QR;
import fr.lehtto.jaser.dns.entity.enumration.RCode;
import fr.lehtto.jaser.dns.entity.enumration.Type;
import fr.lehtto.jaser.dns.entity.parser.InvalidDnsZoneEntryException;
import fr.lehtto.jaser.dns.entity.rdata.internet.ARdata;
import fr.lehtto.jaser.dns.master.file.MasterFile;
import fr.lehtto.jaser.dns.query.handler.QueryHandlerFactory;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class StepDefinitions {


  private Query query;
  private Response response;

  @Before
  public void before() throws InvalidDnsZoneEntryException {
    // Initialize the master file with the dummy zones
    final MasterFile masterFile = new MasterFile();

    masterFile.addRecord(ResourceRecord.builder()
        .name(DomainName.of("www.example.com"))
        .type(Type.A)
        .recordClass(DnsClass.IN)
        .ttl(1)
        .data(new ARdata(AddressV4.of("192.168.1.1")))
        .build());

    Dns.INSTANCE.initializeMasterFiles(masterFile);
  }

  @Given("I have {string} as hostname")
  public void iHaveHostname(final String hostname) {
    this.query = Query.builder()
        .header(Header.builder()
            .id((short) 1)
            .flags(Flags.builder()
                .qr(QR.QUERY)
                .opcode(OpCode.QUERY)
                .rcode(RCode.NO_ERROR)
                .aa(false)
                .tc(false)
                .rd(false)
                .ra(false)
                .z((byte) 0)
                .build())
            .qdcount((short) 1)
            .build())
        .questions(List.of(Question.builder()
            .name(DomainName.of(hostname))
            .recordClass(DnsClass.IN)
            .type(Type.A)
            .build()))
        .build();
  }

  @When("I query the IP address of the hostname")
  public void iQueryTheIPAddressOfTheHostname() {
    response = QueryHandlerFactory.fromQuery(query).handle(query);
  }

  @Then("I should see {string} as IP address of the hostname")
  public void iShouldSeeTheIPAddressOfTheHostname(final String expectedIpAddress) {
    assertNotNull(response, "Response is null");
    assertEquals(1, response.answerRecords().size(), "Answer records size is not 1");
    assertTrue(response.answerRecords().get(0).data() instanceof ARdata, "Answer record data is not ARdata");
    final AddressV4 address = ((ARdata) response.answerRecords().get(0).data()).address();
    assertEquals(expectedIpAddress, address.toDottedDecimal(), "IP address is not correct");
  }

}
