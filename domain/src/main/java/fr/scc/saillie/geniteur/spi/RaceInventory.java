package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;

public interface RaceInventory {
    Race byGeniteurId(Integer id) throws GeniteurException;
}
