package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import fr.scc.saillie.geniteur.model.Geniteur;

/**
 * ReglementGeniteur2023 : règlement en cours
 *
 * @author anthonydenecheau
 */
public class ReglementGeniteur2023 extends AbstractReglementGeniteur {

    @Override
    protected boolean hasValidProfileAdn(Geniteur geniteur, LocalDate dateSaillie, LocalDate dateDerogationAdn, boolean isCommmandeAdnEnCours) {
        return geniteur.hasValidProfileAdn(dateSaillie, dateDerogationAdn, isCommmandeAdnEnCours);
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
