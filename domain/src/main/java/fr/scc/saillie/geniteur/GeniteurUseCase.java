package fr.scc.saillie.geniteur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.LEVEL;
import fr.scc.saillie.geniteur.model.Message;
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

    public GeniteurUseCase(PersonneInventory personneInventory, GeniteurInventory geniteurInventory, RaceInventory raceInventory) {
        this.personneInventory = personneInventory;
        this.geniteurInventory = geniteurInventory;
        this.raceInventory = raceInventory;
    }
    
    /** 
     * Validation de l'ensemble des règles métier pour un géniteur impliqué dans une saillie
     * @param dateSaillie date de saillie
     * @param geniteur données du {@link fr.scc.saillie.geniteur.model.Geniteur}
     * @return List liste des {@link fr.scc.saillie.geniteur.model.Message} (Info,Warning,Error) 
     * @throws GeniteurException
     */
    @Override
    public List<Message> execute(LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException {
        
        List<Message> messages = new ArrayList<Message>();
        
        try {
            // Lecture des informations du géniteur
            Geniteur _g = geniteurInventory.byId(geniteur.id());

            // Contrôle que le sexe annoncé est correct
            if (!geniteur.sexe().equals(_g.sexe())) {
                messages.add(new Message(LEVEL.ERROR,"910","le géniteur n'est pas du bon sexe"));
                return messages;
            }
            
            // Contrôle la date de décès pour la femelle
            if (!_g.isAliveWhenSaillieHasBeenDone(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"940","la lice est déclarée morte à la date de saillie"));
                return messages;
            }

            // Contrôle du type d'inscription
            if (_g.isRegisteredAsProvoisire()) {
                messages.add(new Message(LEVEL.ERROR,"950","le géniteur est inscrit à titre provisoire"));
                return messages;
            }

            // Contrôle des dates
            if (!_g.isValidDateNaissance(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"930","le géniteur est née après la saillie"));
                return messages;
            }
            if (!_g.hasAgeMinimumToReproduce(dateSaillie, raceInventory.byId(_g.idRace()).ageMinimum())) {
                messages.add(new Message(LEVEL.ERROR,"920","le géniteur n'est pas en âge de reproduire"));
                return messages;
            }
            if (_g.isTooOldToReproduce(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"960","la lice est trop âgée pour reproduire"));
                return messages;
            }

            // Validation OK
            messages.add(new Message(LEVEL.INFO,"01","le géniteur est validé"));

        } catch (Exception e) {
            messages.add(new Message(LEVEL.ERROR,"900",e.getMessage()));
        }
        return messages;    
    }
}
