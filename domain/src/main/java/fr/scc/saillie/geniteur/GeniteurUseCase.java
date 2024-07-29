package fr.scc.saillie.geniteur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.LEVEL;
import fr.scc.saillie.geniteur.model.MESSAGE_APPLICATION;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;
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

            // Lecture des litiges sur l'éleveur
            Personne eleveur = personneInventory.byId(idEleveur, PROFIL.ELEVEUR);
            if (eleveur.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.ELEVEUR_LITIGE.code,MESSAGE_APPLICATION.ELEVEUR_LITIGE.message));
                return messages;
            }

            // Lecture des informations du géniteur
            Geniteur _g = geniteurInventory.byId(geniteur.getId());

            // Contrôle que le sexe annoncé est correct
            if (!geniteur.getSexe().equals(_g.getSexe())) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_SEXE.code,MESSAGE_APPLICATION.GENITEUR_SEXE.message));
                return messages;
            }
            
            // Contrôle la date de décès pour la femelle
            if (!_g.isAliveWhenSaillieHasBeenDone(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_DECES.code,MESSAGE_APPLICATION.GENITEUR_DECES.message));
                return messages;
            }

            // Contrôle du nombre maximum de portées autorisées pour la femelle
            if (_g.hasReachedMaxPortee()) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_MAX_PORTEES.code,MESSAGE_APPLICATION.GENITEUR_MAX_PORTEES.code));
                return messages;
            }

            // Alerte s/ le nombre maximum de portées autorisées pour la femelle
            if (_g.isClosedToReachedMaxPortee()) {
                messages.add(new Message(LEVEL.WARNING,MESSAGE_APPLICATION.GENITEUR_ALERTE_PORTEES.code,MESSAGE_APPLICATION.GENITEUR_ALERTE_PORTEES.message));
            }
            
            // Contrôle du type d'inscription
            if (_g.isRegisteredAsProvoisire()) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_PROVISOIRE.code,MESSAGE_APPLICATION.GENITEUR_PROVISOIRE.message));
                return messages;
            }

            // Contrôle des dates
            if (!_g.isValidDateNaissance(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_DATE_NAISSANCE.code,MESSAGE_APPLICATION.GENITEUR_DATE_NAISSANCE.message));
                return messages;
            }
            if (!_g.hasAgeMinimumToReproduce(dateSaillie, raceInventory.byId(_g.getIdRace()).ageMinimum())) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_TROP_JEUNE.code,MESSAGE_APPLICATION.GENITEUR_TROP_JEUNE.message));
                return messages;
            }
            if (_g.isTooOldToReproduce(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_TROP_AGE.code,MESSAGE_APPLICATION.GENITEUR_TROP_AGE.message));
                return messages;
            }

            // La lice n'a pas fait de saillie depuis 5 mois
            if (!_g.isSaillieLiceDelai(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_DELAI.code,MESSAGE_APPLICATION.GENITEUR_DELAI.message));
                return messages;
            }

            // Contrôle des litiges s/ le propriétaire
            Personne proprietaire = personneInventory.byId(_g.getId(), PROFIL.PROPRIETAIRE);
            if (proprietaire != null && proprietaire.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.PROPRIETAIRE_LITIGE.code,MESSAGE_APPLICATION.PROPRIETAIRE_LITIGE.message));
                return messages;
            }    

            // Contrôle des litiges s/ le géniteur
            if (_g.hasLitige(dateSaillie)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_LITIGE.code,MESSAGE_APPLICATION.GENITEUR_LITIGE.message));
                return messages;
            }

            // Lecture des information de la confirmation
            if (_g.getConfirmation() != null) {
                // le chien est en appel de sa confirmation
                if (_g.getConfirmation().numDossier() > 0 && _g.getConfirmation().isOnAppel()) {
                    messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_APPEL_CONFIRMATION.code,MESSAGE_APPLICATION.GENITEUR_APPEL_CONFIRMATION.message));
                    return messages;
                }
                // un dossier de confirmation a été initié mais le chien a été ajourné ou déclaré inapte
                if (_g.getConfirmation().numDossier() > 0 && (_g.getConfirmation().isAjourne() || !_g.getConfirmation().isConfirme())) {
                    messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_INAPTE_CONFIRMATION.code,MESSAGE_APPLICATION.GENITEUR_INAPTE_CONFIRMATION.message));
                    return messages;
                }
            }            

            // Le géniteur n'est pas confirmé ici == exception pour le géniteur non confirmé
            if (_g.getConfirmation() == null && !_g.isExceptionConfirme(dateSaillie, eleveur, proprietaire)) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_NON_CONFIRME.code,MESSAGE_APPLICATION.GENITEUR_NON_CONFIRME.message));
                return messages;
            }

            // Controle que la généalogie du géniteur est complète sur 3 générations
            if (!_g.isGenealogieComplete()) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_GENEALOGIE.code,MESSAGE_APPLICATION.GENITEUR_GENEALOGIE.message));
                return messages;
            }

            // Controle que l'empreinte ADN du géniteur est enregistrée
            if (!_g.hasValidProfileAdn(dateSaillie, raceInventory.byId(_g.getIdRace()).dateDerogationAdn(), adnInventory.isCommandeAdnEnCours(_g.getId()))) {
                messages.add(new Message(LEVEL.ERROR,MESSAGE_APPLICATION.GENITEUR_EMPREINTE.code,MESSAGE_APPLICATION.GENITEUR_EMPREINTE.message));
                return messages;
            }
            
            // Validation OK
            messages.add(new Message(LEVEL.INFO,MESSAGE_APPLICATION.VALIDE.code,MESSAGE_APPLICATION.VALIDE.message));

        } catch (Exception e) {
            messages.add(new Message(LEVEL.ERROR,"900",e.getMessage()));
        }
        return messages;    
    }
}
