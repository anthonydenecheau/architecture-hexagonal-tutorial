package fr.scc.saillie.geniteur.spi.stubs;

import java.text.ParseException;

import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;
import fr.scc.saillie.geniteur.spi.RaceInventory;

@Stub
public class RaceInventoryStub implements RaceInventory {

    private static final Race DEFAULT_RACE;

    static {
        try {
            DEFAULT_RACE = initialiserRace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private final Race race;

    public RaceInventoryStub() {
        race = DEFAULT_RACE;
    }

    public RaceInventoryStub(Race race) {
        this.race = race;
    }

    private static Race initialiserRace() throws ParseException {
        return new Race(56, 12);
    }

    @Override
    public Race byId(Integer id) throws GeniteurException {
        return race;
    }
    
}
