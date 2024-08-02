package fr.scc.saillie.icadapi;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.IcadInventory;

@RestClientTest(IcadClient.class)
public class IcadClientTest {

    @Autowired
    private IcadInventory icadClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("classpath:__files/payloads/icad.xml")
    private Resource icadResponse;

    @Test
    void should_return_the_icad_inventory() {
        configureMockServer();

        try {
            Geniteur _g = icadClient.byIdentifiant("2DND115", null);
            assertThat(_g.getDateDeces()).isEqualTo("2024-04-06");
        } catch (GeniteurException e) {
            e.printStackTrace();
        }
    }

    private void configureMockServer() {
        this.mockServer
                .expect(method(HttpMethod.GET))
                .andExpect(requestTo("http://icad/api?l=&m=&ci=&ct=2DND115"))
                .andRespond(withSuccess(icadResponse, APPLICATION_XML));

    }

}
