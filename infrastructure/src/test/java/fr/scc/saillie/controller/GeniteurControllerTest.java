package fr.scc.saillie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.scc.saillie.config.DomainConfiguration;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(GeniteurController.class)
@Import(DomainConfiguration.class)
public class GeniteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Step4")
    public void whenPostRequestAndNotValidGeniteur_thenCorrectReponse() throws Exception {
        String geniteur = "{\"id\": 1, \"dateSaillie\" : null, \"dateNaissance\" : \"01/01/2021\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(geniteur)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Validation failed")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
                ;
    }

    @Test
    @DisplayName("Step4")
    public void whenPostRequestAndValidGeniteurAndNotAuthorize_thenCorrectReponse() throws Exception {
        String geniteur = "{\"id\": 1, \"dateSaillie\" : \"01/01/2021\", \"dateNaissance\" : \"01/01/2023\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(geniteur)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
                ;
        assertThat(result.getResponse().getErrorMessage()).isEqualTo("le géniteur est née avant la saillie");
    }

    @Test
    @DisplayName("Step4")
    public void whenPostRequestAndValidGeniteurAndAuthorize_thenCorrectReponse() throws Exception {
        String geniteur = "{\"id\": 1, \"dateSaillie\" : \"01/01/2024\", \"dateNaissance\" : \"01/01/2022\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/validateGeniteur")
                .content(geniteur)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                ;
    }

}
