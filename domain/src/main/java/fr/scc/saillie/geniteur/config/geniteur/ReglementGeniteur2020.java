package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import fr.scc.saillie.geniteur.model.Geniteur;

/**
 * ReglementGeniteurDefault : règlement par défaut
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
}