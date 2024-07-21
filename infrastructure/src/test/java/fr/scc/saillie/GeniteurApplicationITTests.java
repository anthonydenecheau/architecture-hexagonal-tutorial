package fr.scc.saillie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.FileCopyUtils;

import fr.scc.saillie.config.DomainConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DomainConfiguration.class)
public class GeniteurApplicationITTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:__files/payloads/geniteur-default.json")
    private Resource geniteur_default;

    @Value("classpath:__files/payloads/geniteur-error.json")
    private Resource geniteur_error;

    @Value("classpath:__files/payloads/geniteur-date-naissance.json")
    private Resource geniteur_date_naissance;

    @Value("classpath:__files/payloads/geniteur-trop-jeune.json")
    private Resource geniteur_trop_jeune;

    @Test
    @DisplayName("Step8")
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
    @DisplayName("Step8")
    public void whenPostRequestAndValidGeniteurAndDateNaissanceErreur_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_date_naissance))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString("le géniteur est née après la saillie"))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step8")
    public void whenPostRequestAndValidGeniteurAndAuthorize_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_default))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString("le géniteur est validé"))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step6")
    public void whenPostRequestAndValidGeniteurAndTropJeune_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_trop_jeune))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString("le géniteur n'est pas en âge de reproduire"))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @TestConfiguration
    @ComponentScan(
        basePackages = {"fr.scc.saillie.geniteur","fr.scc.saillie.repository"})   
    static class TestsConfiguration {
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
      
    }

    private String asString(Resource resource) {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

