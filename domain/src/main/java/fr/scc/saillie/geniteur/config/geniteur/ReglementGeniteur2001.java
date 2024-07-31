package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.utils.DateUtils;

/**
 * ReglementGeniteur2001 : règlement 2001 - 2019
 *
 * @author anthonydenecheau
 */
public class ReglementGeniteur2001 extends AbstractReglementGeniteur {

    @Override
    protected boolean hasValidProfileAdn(Geniteur geniteur, LocalDate dateSaillie, LocalDate dateDerogationAdn,
            boolean isCommmandeAdnEnCours) {
        // le contrôle n'est pas effectué dans ce contexte
        return true;
    }

    @Override
    protected boolean hasReachedMaxPortee(Geniteur geniteur) {
        // le contrôle n'est pas effectué dans ce contexte
        return false;
    }

    @Override
    protected boolean isClosedToReachedMaxPortee(Geniteur geniteur) {
        // le contrôle n'est pas effectué dans ce contexte
        return false;
    }
    @Override
    protected boolean isTooOldToReproduce(Geniteur geniteur, LocalDate dateSaillie) {
        // le contrôle n'est pas effectué dans ce contexte
        return false;
    }

    @Override
    protected boolean hasAgeMinimumToReproduce(Geniteur geniteur, LocalDate dateSaillie, String race, int ageMinimum) {
        return DateUtils.getMonthsBetween(geniteur.getDateNaissance(), dateSaillie) > ageMinimum;
    }    

}
