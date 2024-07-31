package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import fr.scc.saillie.geniteur.model.Geniteur;

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

}
