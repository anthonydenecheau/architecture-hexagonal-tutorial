package fr.scc.saillie.spi;

import fr.scc.saillie.model.Race;

public interface RaceInventory {
    Race byGeniteurId(Integer id);
}
