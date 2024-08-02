package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fr.scc.saillie.geniteur.spi.IcadInventory;
import fr.scc.saillie.geniteur.utils.DateUtils;

/**
 * Classe geniteur
 *
 * @author anthonydenecheau
 */
public class Geniteur  {
    int id; 
    int idRace;
    String tatouage;
    String puce;
    LocalDate dateNaissance;
    LocalDate dateDeces;
    TYPE_INSCRIPTION typeInscription;
    SEXE sexe;
    Confirmation confirmation;
    List<Litige> litiges;
    List<Portee> portees;
    boolean genealogieComplete;
    boolean empreinteAdn;

    public Geniteur(int id, int idRace, String tatouage, String puce, LocalDate dateNaissance, LocalDate dateDeces, TYPE_INSCRIPTION typeInscription,
      SEXE sexe, Confirmation confirmation, List<Litige> litiges, List<Portee> portees, boolean genealogieComplete, boolean empreinteAdn) {
        this.id = id;
        this.idRace = idRace;
        this.tatouage = tatouage;
        this.puce = puce;
        this.dateNaissance = dateNaissance;
        this.dateDeces = dateDeces;
        this.typeInscription = typeInscription;
        this.sexe = sexe;
        this.confirmation = confirmation;
        this.litiges = litiges;
        this.portees = portees;
        this.genealogieComplete = genealogieComplete;
        this.empreinteAdn = empreinteAdn;
    }

    public Geniteur(int id, SEXE sexe) {
        this.id = id;
        this.sexe = sexe;
    }

    public void withConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public void withPortees(List<Portee> portees) {
        this.portees = portees;
    }

    public void withLitiges(List<Litige> litiges) {
        this.litiges = litiges;
    }

    public int getId() {
        return id;
    }

    public SEXE getSexe() {
        return sexe;
    }

    public int getIdRace() {
        return idRace;
    }

    public String getTatouage() {
        return tatouage;
    }

    public String getPuce() {
        return puce;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public LocalDate getDateDeces() {
        return dateDeces;
    }

    public void setDateDeces(LocalDate dateDeces) {
        this.dateDeces = dateDeces;
    }

    public TYPE_INSCRIPTION getTypeInscription() {
        return typeInscription;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public boolean isGenealogieComplete() {
        return genealogieComplete;
    }

    public void setGenealogieComplete(boolean genealogieComplete) {
        this.genealogieComplete = genealogieComplete;
    }

    public boolean isEmpreinteAdn() {
        return empreinteAdn;
    }

    public void setEmpreinteAdn(boolean empreinteAdn) {
        this.empreinteAdn = empreinteAdn;
    }

    private static final int MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES = 9;
    private static final int DELAI_AUTORISE = 5;
    private static final int MAX_PORTEES = 8;
    public  static final int MIN_AGE_LICE_EN_MOIS = 15;

    /** 
     * <p>la lice n'a pas déclarée de nouvelle saillie depuis 5 mois</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isSaillieLiceDelai(LocalDate dateSaillie) {
        if (SEXE.FEMELLE.equals(sexe)) {
            if (this.portees == null || this.portees.isEmpty())
                return true;
            
            List<LocalDate> datesSaillie = this.portees.stream()
                .filter(Portee::isEligibleForControle)
                .map( p ->p.dateSaillie())
                .collect(Collectors.toList())
            ;
            LocalDate dateDerniereSaillie = DateUtils.getMostRecentBeforeDate(LocalDate.now(), datesSaillie);
            return DateUtils.getMonthsBetween(dateDerniereSaillie, dateSaillie) >= DELAI_AUTORISE;

        } else
            return true;
    }

    /** 
     * <p>la lice est vivante à la date de saillie</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isAliveWhenSaillieHasBeenDone(LocalDate dateSaillie, IcadInventory icadInventory) {
        if (SEXE.FEMELLE.equals(sexe)) {
            if (dateDeces == null) {
                // appel Icad avec tatouage, puce
                try {
                    LocalDate _d = icadInventory.byIdentifiant(this.tatouage, this.puce).getDateDeces();
                    return _d.isAfter(dateSaillie);
                }  catch (Exception e) {
                    ;
                }
                return true;
            } else {
                return dateDeces.isAfter(dateSaillie);
            }
        } else
            return true;
    }

     /** 
     * <p>le géniteur est inscrit à titre provisoire </p>
     * @return boolean
     */
    public boolean isRegisteredAsProvoisire() {
        return this.typeInscription.equals(TYPE_INSCRIPTION.PROVISOIRE);
    }

    /** 
     * <p>la date de naissance du géniteur est antérieure à la date de saillie</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isValidDateNaissance (LocalDate dateSaillie) {
        return this.dateNaissance.isBefore(dateSaillie);
    }

    /** 
     * <p>la lice ne doit pas être âgée de plus de 9 mois </p>
     * @see MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isTooOldToReproduce(LocalDate dateSaillie) {
        if (SEXE.FEMELLE.equals(this.sexe))
            return DateUtils.getMonthsBetween(this.dateNaissance, dateSaillie) >= MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES * 12;
        else
            return false;
    }

    /** 
     * <p>Liste des exceptions s/ le géniteur qui n'est pas confirmé</p>
     * @param eleveur données de l'éleveur (saillie)
     * @param proprietaire données du propriétaire (géniteur)
     * @return boolean
     */
    public boolean isExceptionConfirme(LocalDate dateSaillie, Personne eleveur, Personne proprietaire) {
    
        // le géniteur est une femelle et que l'éleveur déclarant réside dans les DOMTOM ou à l'étranger 
        if (SEXE.FEMELLE.equals(this.sexe) && (eleveur.isResidantDOMTOM() || eleveur.isResidantEtranger())) {
            return true;
        }
        // le géniteur est un mâle et que le propriétaire réside dans les DOMTOM ou à l'étanger
        if (SEXE.MALE.equals(this.sexe) && (proprietaire.isResidantDOMTOM() || proprietaire.isResidantEtranger()))
            return true;
        // le géniteur est inscrit au titre du LIVRE D'ATTENTE
        if (TYPE_INSCRIPTION.LIVRE_ATTENTE.equals(this.typeInscription))
            return true; 
        // le géniteur est un mâle et inscrit au titre du LIVRE ETRANGER
        if (SEXE.MALE.equals(this.sexe) && TYPE_INSCRIPTION.ETRANGER.equals(typeInscription))
            return true;
            
        return false;
    }

    /** 
     * <p>Présence de litiges à la date de saillie ou litige ouvert</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean hasLitige(LocalDate dateSaillie) {
        if (this.litiges == null)
            return false;

        return this.litiges.stream()
                .anyMatch(litige -> litige.dateOuverture().isBefore(dateSaillie)
                        && ( (litige.dateFermeture() == null ? true : litige.dateFermeture().isAfter(dateSaillie))) );

    }

    /** 
     * <p>la lice a atteint le nombre maximum de saillies autorisées</p>
     * @return boolean
     */
    public boolean hasReachedMaxPortee() {
        if (this.portees == null) {
            return false;
        }
        if (SEXE.FEMELLE.equals(this.sexe))
            return this.portees.size() == MAX_PORTEES;
        else
            return false;
    }

    /** 
     * <p>la lice fait sa dernière saillie</p>
     * @return boolean
     */
    public boolean isClosedToReachedMaxPortee() {
        if (this.portees == null) {
            return false;
        }
        if (SEXE.FEMELLE.equals(this.sexe))
            return this.portees.size() == MAX_PORTEES - 1;
        else
            return false;
    }

    /** 
     * <p>l'empreinte Adn du géniteur est enregistrée ou le géniteur entre dans une exception </p>
     * @return boolean
     */
    public boolean hasValidProfileAdn(LocalDate dateSaillie, LocalDate dateDerogrationControleADN, boolean isCommandeAdnEnCours) {
        
        // si le chien appartient à une race qui est exemptée de l'obligation de l'empreinte ADN, alors l'empreinte ADN est valide
        // la date de saillie de saillie est postérieure à la date de dérogation de l'obligation de l'empreinte ADN            
        if (dateDerogrationControleADN != null && dateDerogrationControleADN.isBefore(dateSaillie))
            return true;

        // si le dossier a été exempté de l'obligation de l'empreinte ADN par le service ADN, alors l'empreinte ADN est valide
        // [TODO] la DS a été saisie

        // si une commande ADN est en cours de traitement pour le chien, alors l'empreinte ADN est valide
        if (isCommandeAdnEnCours)
            return true;
        
        return this.isEmpreinteAdn();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + idRace;
        result = prime * result + ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
        result = prime * result + ((dateDeces == null) ? 0 : dateDeces.hashCode());
        result = prime * result + ((typeInscription == null) ? 0 : typeInscription.hashCode());
        result = prime * result + ((sexe == null) ? 0 : sexe.hashCode());
        result = prime * result + ((confirmation == null) ? 0 : confirmation.hashCode());
        result = prime * result + ((litiges == null) ? 0 : litiges.hashCode());
        result = prime * result + ((portees == null) ? 0 : portees.hashCode());
        result = prime * result + (genealogieComplete ? 1231 : 1237);
        result = prime * result + (empreinteAdn ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Geniteur other = (Geniteur) obj;
        if (id != other.id)
            return false;
        if (idRace != other.idRace)
            return false;
        if (dateNaissance == null) {
            if (other.dateNaissance != null)
                return false;
        } else if (!dateNaissance.equals(other.dateNaissance))
            return false;
        if (dateDeces == null) {
            if (other.dateDeces != null)
                return false;
        } else if (!dateDeces.equals(other.dateDeces))
            return false;
        if (typeInscription != other.typeInscription)
            return false;
        if (sexe != other.sexe)
            return false;
        if (confirmation == null) {
            if (other.confirmation != null)
                return false;
        } else if (!confirmation.equals(other.confirmation))
            return false;
        if (litiges == null) {
            if (other.litiges != null)
                return false;
        } else if (!litiges.equals(other.litiges))
            return false;
        if (portees == null) {
            if (other.portees != null)
                return false;
        } else if (!portees.equals(other.portees))
            return false;
        if (genealogieComplete != other.genealogieComplete)
            return false;
        if (empreinteAdn != other.empreinteAdn)
            return false;
        return true;
    }

    
}
