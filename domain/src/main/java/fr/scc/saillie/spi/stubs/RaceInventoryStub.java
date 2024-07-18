package fr.scc.saillie.spi.stubs;

import java.text.ParseException;

import fr.scc.ddd.Stub;
import fr.scc.saillie.model.Race;
import fr.scc.saillie.spi.RaceInventory;

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
    public Race byGeniteurId(Integer id) {
        return race;
    }
    
}
