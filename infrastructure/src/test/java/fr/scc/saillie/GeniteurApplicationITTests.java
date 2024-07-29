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
import fr.scc.saillie.geniteur.model.MESSAGE_APPLICATION;

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

    @Value("classpath:__files/payloads/geniteur-date-deces.json")
    private Resource geniteur_date_deces;

    @Value("classpath:__files/payloads/geniteur-inscription-provisoire.json")
    private Resource geniteur_inscription_provisoire;

    @Value("classpath:__files/payloads/geniteur-trop-agee.json")
    private Resource geniteur_trop_agee;

    @Value("classpath:__files/payloads/geniteur-eleveur-litige.json")
    private Resource geniteur_eleveur_litige;

    @Value("classpath:__files/payloads/geniteur-lice-saillie.json")
    private Resource geniteur_lice_saillie;

    @Value("classpath:__files/payloads/geniteur-lice-litige.json")
    private Resource geniteur_lice_litige;

    @Value("classpath:__files/payloads/geniteur-confirmation-appel.json")
    private Resource geniteur_confirmation_appel;

    @Value("classpath:__files/payloads/geniteur-confirmation-inapte.json")
    private Resource geniteur_confirmation_inapte;

    @Value("classpath:__files/payloads/geniteur-empreinte-adn.json")
    private Resource geniteur_empreinte_adn;

    @Value("classpath:__files/payloads/geniteur-genealogie-incomplete.json")
    private Resource geniteur_genealogie_incomplete;

    @Value("classpath:__files/payloads/geniteur-genealogie-complete-titre-initial.json")
    private Resource geniteur_genealogie_complete_titre_initial;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_DATE_NAISSANCE.message))))
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.VALIDE.message))))
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_TROP_JEUNE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGeniteurAndLiceDeces_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_date_deces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_DECES.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGeniteurAndInscriptionProvisoire_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_inscription_provisoire))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_PROVISOIRE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGeniteurAndTropAgee_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_trop_agee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_TROP_AGE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGeniteurAndEleveurLitige_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_eleveur_litige))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.ELEVEUR_LITIGE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }    

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidLiceSaillie_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_lice_saillie))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_DELAI.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }    

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidLiceLitige_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_lice_litige))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_LITIGE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    } 

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidConfirmationAppel_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_confirmation_appel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_APPEL_CONFIRMATION.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    }     

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidConfirmationInapte_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_confirmation_inapte))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_INAPTE_CONFIRMATION.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    } 

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidMissingEmpreinteAdn_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_empreinte_adn))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_EMPREINTE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    } 

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGenealogieIncomplete_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_genealogie_incomplete))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.GENITEUR_GENEALOGIE.message))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
                ;
    } 

    @Test
    @DisplayName("Step9")
    public void whenPostRequestAndValidGenealogieTitreInitial_thenCorrectReponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(asString(geniteur_genealogie_complete_titre_initial))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.anyOf(Matchers.containsString(MESSAGE_APPLICATION.VALIDE.message))))
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

