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
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;
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
    public List<Message> execute(int idEleveur, LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException {
        
        List<Message> messages = new ArrayList<Message>();
        
        try {

            // Lecture des litiges sur l'éleveur
            Personne eleveur = personneInventory.byId(idEleveur, PROFIL.ELEVEUR);
            if (eleveur.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"971","l'éleveur a un litige"));
                return messages;
            }

            // Lecture des informations du géniteur
            Geniteur _g = geniteurInventory.byId(geniteur.getId());

            // Contrôle que le sexe annoncé est correct
            if (!geniteur.getSexe().equals(_g.getSexe())) {
                messages.add(new Message(LEVEL.ERROR,"910","le géniteur n'est pas du bon sexe"));
                return messages;
            }
            
            // Contrôle la date de décès pour la femelle
            if (!_g.isAliveWhenSaillieHasBeenDone(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"940","la lice est déclarée morte à la date de saillie"));
                return messages;
            }

            // Contrôle du nombre maximum de portées autorisées pour la femelle
            if (_g.hasReachedMaxPortee()) {
                messages.add(new Message(LEVEL.ERROR,"977","la lice a déjà fait 8 portées avec des chiots inscrits au LOF"));
                return messages;
            }

            // Alerte s/ le nombre maximum de portées autorisées pour la femelle
            if (_g.isClosedToReachedMaxPortee()) {
                messages.add(new Message(LEVEL.WARNING,"978","la portée sera la 8ème portée, ce sera donc la dernière portée pour la lice"));
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
            if (!_g.hasAgeMinimumToReproduce(dateSaillie, raceInventory.byId(_g.getIdRace()).ageMinimum())) {
                messages.add(new Message(LEVEL.ERROR,"920","le géniteur n'est pas en âge de reproduire"));
                return messages;
            }
            if (_g.isTooOldToReproduce(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"960","la lice est trop âgée pour reproduire"));
                return messages;
            }

            // La lice n'a pas fait de saillie depuis 5 mois
            if (!_g.isSaillieLiceDelai(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"975","une saillie a déjà eu lieu lors des 5 derniers mois pour cette lice"));
                return messages;
            }

            // Contrôle des litiges s/ le propriétaire
            Personne proprietaire = personneInventory.byId(_g.getId(), PROFIL.PROPRIETAIRE);
            if (proprietaire.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"976","le propriétaire du géniteur a un litige"));
                return messages;
            }    

            // Contrôle des litiges s/ le géniteur
            if (_g.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,"972","le géniteur possède des litiges"));
                return messages;
            }

            // Lecture des information de la confirmation
            if (_g.getConfirmation() != null) {
                // le chien est en appel de sa confirmation
                if (_g.getConfirmation().numDossier() > 0 && _g.getConfirmation().isOnAppel()) {
                    messages.add(new Message(LEVEL.ERROR,"973","le géniteur a un appel sur la confirmation"));
                    return messages;
                }
                // un dossier de confirmation a été initié mais le chien a été ajourné ou déclaré inapte
                if (_g.getConfirmation().numDossier() > 0 && (_g.getConfirmation().isAjourne() || !_g.getConfirmation().isConfirme())) {
                    messages.add(new Message(LEVEL.ERROR,"974","le géniteur a été ajourné ou déclaré inapte à la confirmation"));
                    return messages;
                }
            }            

            // Le géniteur n'est pas confirmé ici == exception pour le géniteur non confirmé
            if (_g.getConfirmation() == null && !_g.isExceptionConfirme(dateSaillie, eleveur, proprietaire)) {
                messages.add(new Message(LEVEL.ERROR,"970","le géniteur n'est pas confirmé"));
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
