package fr.scc.saillie.geniteur.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import fr.scc.saillie.geniteur.config.geniteur.IReglementationGeniteur;
import fr.scc.saillie.geniteur.config.geniteur.ReglementGeniteur2001;
import fr.scc.saillie.geniteur.config.geniteur.ReglementGeniteur2020;
import fr.scc.saillie.geniteur.config.geniteur.ReglementGeniteur2023;
import fr.scc.saillie.geniteur.error.ConfigException;
import fr.scc.saillie.geniteur.utils.DateUtils;

import static java.util.Arrays.asList;

/**
 * ReglementationFactory - Détermine la règlementation à appliquer
 *
 * @author anthonydenecheau
 */
public class ReglementationFactory {

    /** 
     * Recherche de la règlementation spécifique aux règles de la commission élevage à une date donnée
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
                    businessCmd = new ReglementGeniteur2001();
                    break;
                case 1:
                    businessCmd = new ReglementGeniteur2020();
                    break;
                case 2:
                    businessCmd = new ReglementGeniteur2023();
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
        List<Reglement> reglements = asList(
                new Reglement(0, DateUtils.convertStringToLocalDate("01/01/2001"), DateUtils.convertStringToLocalDate("31/12/2019")),
                new Reglement(1, DateUtils.convertStringToLocalDate("01/01/2020"), DateUtils.convertStringToLocalDate("03/09/2023")),
                new Reglement(2, DateUtils.convertStringToLocalDate("04/09/2023"), DateUtils.convertStringToLocalDate("31/12/9999"))
        );
        // select the reglement based on the dateReference  provided
        for (Reglement reglement : reglements) {
            if (dateReference.isAfter(reglement.dateDebut) && dateReference.isBefore(reglement.dateFin)) {
                return reglement.id;
            }
        }
        return -1;
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
