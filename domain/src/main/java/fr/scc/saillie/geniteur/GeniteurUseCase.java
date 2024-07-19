package fr.scc.saillie.geniteur;

import java.time.LocalDate;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.RaceInventory;

/**
 * GeniteurUseCase
 *
 * @author anthonydenecheau
 */
@DomainService
public class GeniteurUseCase implements ValidateGeniteur {

    private final RaceInventory raceInventory;

    public GeniteurUseCase(RaceInventory raceInventory) {
        this.raceInventory = raceInventory;
    }

    
    /** 
     * Validation de l'ensemble des règles métier pour un géniteur impliqué dans une saillie
     * @param dateSaillie date de saillie
     * @param geniteur données du géniteur {@link fr.scc.saillie.geniteur.model.Geniteur}
     * @return String
     * @throws GeniteurException
     */
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
