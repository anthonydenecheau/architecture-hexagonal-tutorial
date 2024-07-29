package fr.scc.saillie.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdnRepositoryTest {

    @Autowired
    AdnRepository adnRepository;

    @Test
    public void should_not_adn_encours() throws Exception {
        //Given
        Integer idGeniteur = 1;
        //When
        //Then
        assertThat(adnRepository.isCommandeAdnEnCours(idGeniteur)).isTrue();
    }    

}
