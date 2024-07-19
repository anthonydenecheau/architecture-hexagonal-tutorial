package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

/**
 * Classe geniteur
 *
 * @author anthonydenecheau
 */
public record Geniteur(int id, LocalDate dateNaissance) {

    
    /** 
     * <p>Determine si la date de naissance du géniteur est antérieure à la date de saillie</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean isValidDateNaissance (LocalDate dateSaillie) {
        return this.dateNaissance.isBefore(dateSaillie);
    }

    
    /** 
     * Determine si à la date de saillie, le géniteur a bien l'âge requis pour effectuer une saillie
     * @see #getMonthsBetween(LocalDate,LocalDate)
     * @param dateSaillie date de saillie
     * @param ageMinimum age minimum requis
     * @return boolean
     */
    public boolean hasAgeMinimum(LocalDate dateSaillie, int ageMinimum) {
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
