package fr.scc.saillie.geniteur;

import java.time.LocalDate;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.RaceInventory;

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
