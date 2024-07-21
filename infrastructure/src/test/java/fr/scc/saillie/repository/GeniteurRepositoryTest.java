package fr.scc.saillie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootTest
public class GeniteurRepositoryTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Autowired
    GeniteurRepository geniteurRepository;

    @Test
    public void should_read_geniteur_byId() throws Exception {
        //Given
        Integer idGeniteur = 1;
        //When
        Geniteur geniteur = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE);
        //Then
        assertThat(geniteurRepository.byId(idGeniteur)).isEqualTo(geniteur);
    }    

    @Test
    public void should_unknown_geniteur_byId() throws Exception {
        //Given
        Integer idGeniteur = 0;
        //When
        //Then
        GeniteurException thrown = assertThrows(
            GeniteurException.class,
            () -> geniteurRepository.byId(idGeniteur),
            "Aucune géniteur trouvé pour le chien : 1"); 
    }    
}
