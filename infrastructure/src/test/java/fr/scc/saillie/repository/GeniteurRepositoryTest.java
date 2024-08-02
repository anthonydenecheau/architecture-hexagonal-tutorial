package fr.scc.saillie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Confirmation;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;
import fr.scc.saillie.geniteur.utils.DateUtils;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GeniteurRepositoryTest {

    @Autowired
    GeniteurRepository geniteurRepository;

    @Test
    public void should_read_geniteur_byId() throws Exception {
        //Given
        Integer idGeniteur = 1;
        Confirmation confirmation = new Confirmation(202300001, 1,  DateUtils.convertStringToLocalDate("01/01/2023"), true, false, false);
        //When
        Geniteur geniteur = new Geniteur(1, 56, "2DND115", null, DateUtils.convertStringToLocalDate("01/01/2022"), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, confirmation, asList(), asList(), true, true);
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
