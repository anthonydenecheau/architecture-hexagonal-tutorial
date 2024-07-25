package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Classe geniteur
 *
 * @author anthonydenecheau
 */
public record Geniteur(int id, int idRace, LocalDate dateNaissance , LocalDate dateDeces, TYPE_INSCRIPTION typeInscription, SEXE sexe, Confirmation confirmation, List<Litige> litiges) {

    public Geniteur(int id, SEXE sexe) {
        this(id, 0, null, null, null, sexe, null, null);
    }

    public Geniteur withConfirmation(Confirmation confirmation) {
        return new Geniteur(id(), idRace(), dateNaissance(), dateDeces(), typeInscription(), sexe(), confirmation, litiges());
    }

    private static final int MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES = 9;

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
        return this.typeInscription().equals(TYPE_INSCRIPTION.PROVISOIRE);
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
     * <p>le géniteur est confirmé</p>
     * @param eleveur données de l'éleveur (saillie)
     * @param proprietaire données du propriétaire (géniteur)
     * @return boolean
     */
    public boolean isExceptionConfirme(LocalDate dateSaillie, Personne eleveur, Personne proprietaire) {
    
        // le géniteur est une femelle et que l'éleveur déclarant réside dans les DOMTOM ou à l'étranger 
        if (SEXE.FEMELLE.equals(sexe()) && (eleveur.isResidantDOMTOM() || eleveur.isResidantEtranger())) {
            return true;
        }
        // le géniteur est un mâle et que le propriétaire réside dans les DOMTOM ou à l'étanger
        if (SEXE.MALE.equals(sexe()) && (proprietaire.isResidantDOMTOM() || proprietaire.isResidantEtranger()))
            return true;
        // le géniteur est inscrit au titre du LIVRE D'ATTENTE
        if (TYPE_INSCRIPTION.LIVRE_ATTENTE.equals(typeInscription()))
            return true; 
        // le géniteur est un mâle et inscrit  au titre du LIVRE ETRANGER
        if (SEXE.MALE.equals(sexe()) && TYPE_INSCRIPTION.ETRANGER.equals(typeInscription))
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
     * <p>Présence de litiges à la date de saillie ou litige ouvert</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean hasLitige(LocalDate dateSaillie) {
        if (this.litiges == null)
            return false;

        return this.litiges.stream()
                .anyMatch(litige -> litige.dateOuverture().isBefore(dateSaillie)
                        && ( litige.dateFermeture().isAfter(dateSaillie) || litige.dateFermeture() == null) );

    }

}
