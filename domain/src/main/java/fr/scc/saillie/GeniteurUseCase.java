package fr.scc.saillie;

import java.time.LocalDate;

import fr.scc.ddd.DomainService;
import fr.scc.saillie.api.ValidateGeniteur;
import fr.scc.saillie.error.GeniteurException;
import fr.scc.saillie.model.Geniteur;
import fr.scc.saillie.spi.RaceInventory;

@DomainService
public class GeniteurUseCase implements ValidateGeniteur {

    private final RaceInventory raceInventory;

    public GeniteurUseCase(RaceInventory raceInventory) {
        this.raceInventory = raceInventory;
    }

    @Override
    public String execute(LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException {
        if (geniteur.isValidDateNaissance(dateSaillie))
            if (geniteur.hasAgeMinimum(dateSaillie, raceInventory.byGeniteurId(geniteur.id()).ageMinimum()))
                return "Le géniteur est validé";
            else
                throw new GeniteurException("le géniteur n'est pas en âge de reproduire",null);
        else 
            throw new GeniteurException("le géniteur est née avant la saillie",null);
    }
}
