package fr.scc.saillie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.model.Race;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RaceRepositoryTest {

    @Autowired
    RaceRepository raceRepository;

    @Test
    public void should_read_race_byIdGeniteur() throws Exception {
        //Given
        Integer idGeniteur = 1;
        //When
        Race race = new Race(56, 12);
        //Then
        assertThat(raceRepository.byGeniteurId(idGeniteur)).isEqualTo(race);
    }    
}
