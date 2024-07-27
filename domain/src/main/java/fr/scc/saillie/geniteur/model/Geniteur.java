package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Classe geniteur
 *
 * @author anthonydenecheau
 */
public class Geniteur  {
    int id; 
    int idRace;
    LocalDate dateNaissance;
    LocalDate dateDeces;
    TYPE_INSCRIPTION typeInscription;
    SEXE sexe;
    Confirmation confirmation;
    List<Litige> litiges;
    List<Portee> portees;

    public Geniteur(int id, int idRace, LocalDate dateNaissance, LocalDate dateDeces, TYPE_INSCRIPTION typeInscription,
    SEXE sexe, Confirmation confirmation, List<Litige> litiges, List<Portee> portees) {
        this.id = id;
        this.idRace = idRace;
        this.dateNaissance = dateNaissance;
        this.dateDeces = dateDeces;
        this.typeInscription = typeInscription;
        this.sexe = sexe;
        this.confirmation = confirmation;
        this.litiges = litiges;
        this.portees = portees;
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

    public int getId() {
        return id;
    }

    public SEXE getSexe() {
        return sexe;
    }

    public int getIdRace() {
        return idRace;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    private static final int MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES = 9;
    private static final int DELAI_AUTORISE = 5;
    private static final int MAX_PORTEES = 8;

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
            LocalDate dateDerniereSaillie = getMostRecentBeforeDate(LocalDate.now(), datesSaillie);
            return getMonthsBetween(dateDerniereSaillie, dateSaillie) >= DELAI_AUTORISE;

        } else
            return true;
    }

    /** 
     * <p>la lice est vivante à la date de saillie</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isAliveWhenSaillieHasBeenDone(LocalDate dateSaillie) {
        if (SEXE.FEMELLE.equals(sexe)) {
            if (dateDeces == null)
                return true;
            else {
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
            return getMonthsBetween(this.dateNaissance, dateSaillie) >= MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES * 12;
        else
            return false;
    }

    /** 
     * <p>le géniteur a bien l'âge requis pour effectuer une saillie</p>
     * @see #getMonthsBetween(LocalDate,LocalDate)
     * @param dateSaillie date de saillie
     * @param ageMinimum age minimum requis
     * @return boolean
     */
    public boolean hasAgeMinimumToReproduce(LocalDate dateSaillie, int ageMinimum) {
        return getMonthsBetween(dateNaissance, dateSaillie) > ageMinimum;
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
     * <p>le nombre de mois séparant 2 dates</p>
     * @param fromDate
     * @param toDate
     * @return long
     */
    private long getMonthsBetween(LocalDate fromDate, LocalDate toDate) {
        return ChronoUnit.MONTHS.between(YearMonth.from(fromDate), YearMonth.from(toDate));
    }

    /** 
     * <p>extraction de la date la plus récente</p>
     * @param targetDate
     * @param dateList
     * @return long
     */
    private static LocalDate getMostRecentBeforeDate(LocalDate targetDate, List<LocalDate> dateList) {
        // we filter the list so that only dates which are "older" than our targeted date remain
        // then we get the most recent date by using compareTo method from LocalDate class and we return that date
        return dateList.stream().filter(date -> date.isBefore(targetDate)).max(LocalDate::compareTo).get();
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
        return true;
    }

}
