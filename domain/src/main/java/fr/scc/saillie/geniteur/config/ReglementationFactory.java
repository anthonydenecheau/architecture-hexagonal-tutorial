package fr.scc.saillie.geniteur.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import fr.scc.saillie.geniteur.config.geniteur.IReglementationGeniteur;
import fr.scc.saillie.geniteur.config.geniteur.ReglementGeniteurDefault;
import fr.scc.saillie.geniteur.error.ConfigException;
import fr.scc.saillie.geniteur.error.GeniteurException;

import static java.util.Arrays.asList;

/**
 * ReglementationFactory - Détermine la règlementation à appliquer
 *
 * @author anthonydenecheau
 */
public class ReglementationFactory {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    /** 
     * Recherche de la règlementation spécifiques aux règles de la commission élevage à une date donnée
     * @param dateReference date de référence pour la règlementation 
     * @return IReglementationGeniteur Règlementation {@link fr.scc.saillie.geniteur.config.geniteur.IReglementationGeniteur}
     * @throws Exception
     */    
    public static IReglementationGeniteur createReglementationGeniteur(LocalDate dateReference) throws Exception {
        IReglementationGeniteur businessCmd = null;
        try {
            int cmdPatternCode = lireReglementationGeniteur(dateReference);
            switch (cmdPatternCode) {
                case 0:
                    businessCmd = new ReglementGeniteurDefault();
                    break;
                default:
                    throw new ConfigException("Aucun reglement geniteur trouvé pour la date de saillie fournie");
            }
        } catch (Exception e) {
            throw e;
        }
        return businessCmd;
    }
    
    /** 
     * Liste des règlementations 
     * @param dateReference date de référence pour la règlementation 
     * @return int 
     * @throws ParseException
     */    
    private static int lireReglementationGeniteur(LocalDate dateReference) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        List<Reglement> reglements = asList(
                new Reglement(0, ConvertStringToLocalDate("01/01/2001"), ConvertStringToLocalDate("31/12/9999"))
        );
        // select the reglement based on the dateReference  provided
        for (Reglement reglement : reglements) {
            if (dateReference.isAfter(reglement.dateDebut) && dateReference.isBefore(reglement.dateFin)) {
                return reglement.id;
            }
        }
        return -1;
    }
    
    private static LocalDate ConvertStringToLocalDate(String val) {
        try {
            return LocalDate.parse(val, formatter);
        } catch (Exception e) {
            return null;
        }
    } 

    /**
    * Reglement
    *
    * @author anthonydenecheau
    */
    public static class Reglement {
        private final int id;
        private final LocalDate dateDebut;
        private final LocalDate dateFin;

        public Reglement(int id, LocalDate dateDebut, LocalDate dateFin) {
            this.id = id;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }
    }
}
