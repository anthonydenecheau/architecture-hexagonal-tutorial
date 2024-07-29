package fr.scc.saillie.geniteur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.config.ReglementationFactory;
import fr.scc.saillie.geniteur.config.geniteur.IReglementationGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.LEVEL;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.spi.AdnInventory;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;
import fr.scc.saillie.geniteur.spi.PersonneInventory;
import fr.scc.saillie.geniteur.spi.RaceInventory;

/**
 * GeniteurUseCase
 *
 * @author anthonydenecheau
 */
@DomainService
public class GeniteurUseCase implements ValidateGeniteur {

    private final RaceInventory raceInventory;
    private final GeniteurInventory geniteurInventory;
    private final PersonneInventory personneInventory;
    private final AdnInventory adnInventory;

    public GeniteurUseCase(PersonneInventory personneInventory, GeniteurInventory geniteurInventory, RaceInventory raceInventory, AdnInventory adnInventory) {
        this.personneInventory = personneInventory;
        this.geniteurInventory = geniteurInventory;
        this.raceInventory = raceInventory;
        this.adnInventory = adnInventory;
    }
    
    /** 
     * Validation de l'ensemble des règles métier pour un géniteur impliqué dans une saillie
     * @param dateSaillie date de saillie
     * @param geniteur données du {@link fr.scc.saillie.geniteur.model.Geniteur}
     * @return List liste des {@link fr.scc.saillie.geniteur.model.Message} (Info,Warning,Error) 
     * @throws GeniteurException
     */
    @Override
    public List<Message> execute(int idEleveur, LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException {

        List<Message> messages = new ArrayList<Message>();

        try {
            // Denier Reglement en cours; il faudrait ajouter l'ancien règlement côté DS (2020) et Adn (2023)
            // Au total ce sont 3 règlements qui doivent être implémentés (car on a fait le choix de regrouper le règlement DS et ADN au sein d'un seul et même règlement)
            IReglementationGeniteur reglementationGeniteur = ReglementationFactory.createReglementationGeniteur(dateSaillie);
            messages = reglementationGeniteur.execute(idEleveur, dateSaillie, geniteur, personneInventory, geniteurInventory, raceInventory, adnInventory);

        } catch (Exception e) {
            messages.add(new Message(LEVEL.ERROR,"900",e.getMessage()));
        }
        return messages;    
    }
}
