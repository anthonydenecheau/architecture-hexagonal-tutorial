package fr.scc.saillie.geniteur.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public final class DateUtils {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    private final static DateTimeFormatter formatterIcad = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.FRENCH);

    /** 
     * <p>le nombre de mois séparant 2 dates</p>
     * @param fromDate
     * @param toDate
     * @return long
     */
    public static long getMonthsBetween(LocalDate fromDate, LocalDate toDate) {
        return ChronoUnit.MONTHS.between(YearMonth.from(fromDate), YearMonth.from(toDate));
    }  

    /** 
     * <p>extraction de la date la plus récente</p>
     * @param targetDate
     * @param dateList
     * @return long
     */
    public static LocalDate getMostRecentBeforeDate(LocalDate targetDate, List<LocalDate> dateList) {
        // we filter the list so that only dates which are "older" than our targeted date remain
        // then we get the most recent date by using compareTo method from LocalDate class and we return that date
        return dateList.stream().filter(date -> date.isBefore(targetDate)).max(LocalDate::compareTo).get();
    }    

    /** 
     * <p>convertion d'une chaine de caractère en LocalDate (format : dd/MM/yyyy)</p>
     * @param String date
     * @return LocaleDate
     */
    public static LocalDate convertStringToLocalDate(String date) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /** 
     * <p>convertion du format date ICad en LocalDate (format : dd/MM/yyyy)</p>
     * @param String date
     * @return LocaleDate
     */
    public static LocalDate convertIcadStringToLocalDate(String date) {
        try {
            return LocalDate.parse(date, formatterIcad);
        } catch (Exception e) {
            return null;
        }
    }

}
