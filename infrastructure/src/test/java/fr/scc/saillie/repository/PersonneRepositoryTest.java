package fr.scc.saillie.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PersonneRepositoryTest {

    @Autowired
    PersonneRepository personneRepository;

    @Test
    public void should_read_eleveur_byId() throws Exception {
        //Given
        Integer idEleveur = 1;
        //When
        Personne personne = new Personne(1, "44", "FRANCE");
        //Then
        assertThat(personneRepository.byId(idEleveur, PROFIL.ELEVEUR)).isEqualTo(personne);
    }    

    @Test
    public void should_read_proprietaire_byId() throws Exception {
        //Given
        Integer idChien = 1;
        //When
        Personne personne = new Personne(1, "44", "FRANCE");
        //Then
        assertThat(personneRepository.byId(idChien, PROFIL.PROPRIETAIRE)).isEqualTo(personne);
    }    

    @Test
    public void should_unknown_eleveur_byId() throws Exception {
        //Given
        Integer idEleveur = 0;
        //When
        //Then
        GeniteurException thrown = assertThrows(
            GeniteurException.class,
            () -> personneRepository.byId(idEleveur, PROFIL.ELEVEUR),
            "Aucune personne trouvée pour : 0"); 
    }    

}
