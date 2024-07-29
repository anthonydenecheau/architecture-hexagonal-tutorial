package fr.scc.saillie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.FileCopyUtils;

import fr.scc.saillie.config.DomainConfiguration;
import fr.scc.saillie.ddd.Stub;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

@WebMvcTest(GeniteurController.class)
@Import(DomainConfiguration.class)
public class GeniteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:__files/payloads/geniteur-default.json")
    private Resource geniteur_default;

    @Value("classpath:__files/payloads/geniteur-error.json")
    private Resource geniteur_error;

    @Test
    @DisplayName("Step4")
    public void whenPostRequestAndMissingDateSaillie_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_error))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString("La date de saillie doit être renseignée"))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step4")
    public void whenPostRequestAndValidGeniteurAndAuthorize_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_default))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString("le géniteur est validé"))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @TestConfiguration
    @ComponentScan(
            basePackages = {"fr.scc.saillie.geniteur"},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }

    private String asString(Resource resource) {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
