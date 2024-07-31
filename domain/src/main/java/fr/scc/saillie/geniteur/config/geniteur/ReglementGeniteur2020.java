package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.utils.DateUtils;

/**
 * ReglementGeniteurDefault : règlement 2020 - 2023
 *
 * @author anthonydenecheau
 */
public class ReglementGeniteur2020 extends AbstractReglementGeniteur {

    @Override
    protected boolean hasValidProfileAdn(Geniteur geniteur, LocalDate dateSaillie, LocalDate dateDerogationAdn,
            boolean isCommmandeAdnEnCours) {
        // le contrôle n'est pas effectué dans ce contexte
        return true;
    }

    @Override
    protected boolean hasReachedMaxPortee(Geniteur geniteur) {
        return geniteur.hasReachedMaxPortee();
    }

    @Override
    protected boolean isClosedToReachedMaxPortee(Geniteur geniteur) {
        return geniteur.isClosedToReachedMaxPortee();
    }

    @Override
    protected boolean isTooOldToReproduce(Geniteur geniteur, LocalDate dateSaillie) {
        return geniteur.isTooOldToReproduce(dateSaillie);
    }

    @Override
    protected boolean hasAgeMinimumToReproduce(Geniteur geniteur, LocalDate dateSaillie, String race, int ageMinimum) {
        if (SEXE.MALE.equals(geniteur.getSexe())) {
            if ("TECKEL".equals(race) && dateSaillie.isBefore(DateUtils.convertStringToLocalDate("01/06/2022")))
                ageMinimum = 12;

            if ( ("AKITA".equals(race) || "STAFFORDSHIRE TERRIER AMERICAIN".equals(race))
                && dateSaillie.isBefore(DateUtils.convertStringToLocalDate("01/01/2023")))                
                ageMinimum = 12;
        }
        if (SEXE.FEMELLE.equals(geniteur.getSexe())) {
            ageMinimum = Geniteur.MIN_AGE_LICE_EN_MOIS;
        }

        return DateUtils.getMonthsBetween(geniteur.getDateNaissance(), dateSaillie) > ageMinimum;

    }    

}