package fr.scc.saillie.repository;

import org.springframework.stereotype.Component;

import fr.scc.saillie.model.Race;
import fr.scc.saillie.spi.RaceInventory;

@Component
public class RaceRepository implements RaceInventory {

    @Override
    public Race byGeniteurId(Integer id) {
        return new Race(56, 12);
    }

}
