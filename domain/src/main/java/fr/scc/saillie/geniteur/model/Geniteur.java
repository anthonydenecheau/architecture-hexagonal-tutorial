package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Classe geniteur
 *
 * @author anthonydenecheau
 */
public record Geniteur(int id, int idRace, LocalDate dateNaissance , LocalDate dateDeces, TYPE_INSCRIPTION typeInscription, SEXE sexe) {

    public Geniteur(int id, SEXE sexe) {
        this(id, 0, null, null, null, sexe);
    }

    private static final int MAX_AGE_LICE_POUR_SAILLIE_EN_ANNEES = 9;

    /** 
     * <p>Determine si la lice etait vivante à la date de saillie</p>
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
     * <p>Determine si le géniteur est inscrit à titre provisoire </p>
     * @return boolean
     */
    public boolean isRegisteredAsProvoisire() {
        return this.typeInscription().equals(TYPE_INSCRIPTION.PROVISOIRE);
    }

    /** 
     * <p>Determine si la date de naissance du géniteur est antérieure à la date de saillie</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isValidDateNaissance (LocalDate dateSaillie) {
        return this.dateNaissance.isBefore(dateSaillie);
    }

    /** 
     * <p>La lice ne doit pas être âgée de plus de 9 mois </p>
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
     * Determine si à la date de saillie, le géniteur a bien l'âge requis pour effectuer une saillie
     * @see #getMonthsBetween(LocalDate,LocalDate)
     * @param dateSaillie date de saillie
     * @param ageMinimum age minimum requis
     * @return boolean
     */
    public boolean hasAgeMinimumToReproduce(LocalDate dateSaillie, int ageMinimum) {
        return getMonthsBetween(dateNaissance, dateSaillie) > ageMinimum;
    }

    /** 
     * Retourne le nombre de mois séparant 2 dates
     * @param fromDate
     * @param toDate
     * @return long
     */
    private long getMonthsBetween(LocalDate fromDate, LocalDate toDate) {
        return ChronoUnit.MONTHS.between(YearMonth.from(fromDate), YearMonth.from(toDate));
    }
}
