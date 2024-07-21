package fr.scc.saillie.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RaceRepositoryTest {

    @Autowired
    RaceRepository raceRepository;

    @Test
    public void should_read_race_byId() throws Exception {
        //Given
        Integer idRace = 56;
        //When
        Race race = new Race(56, 12);
        //Then
        assertThat(raceRepository.byId(idRace)).isEqualTo(race);
    }    

    @Test
    public void should_unknown_race_byId() throws Exception {
        //Given
        Integer idRace = 1;
        //When
        //Then
        GeniteurException thrown = assertThrows(
            GeniteurException.class,
            () -> raceRepository.byId(idRace),
            "Aucune race trouvée pour le chien : 1"); 
    }    

}
