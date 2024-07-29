package fr.scc.saillie.geniteur.spi.stubs;

import java.text.ParseException;

import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Personne;
import fr.scc.saillie.geniteur.spi.AdnInventory;

@Stub
public class AdnInventoryStub implements AdnInventory {

    private static final boolean DEFAULT_COMMANDE_ADN_EN_COURS;

    static {
        DEFAULT_COMMANDE_ADN_EN_COURS = false;
    }
    
    private final boolean commandeAdnEnCours;

    public AdnInventoryStub() {
        commandeAdnEnCours = DEFAULT_COMMANDE_ADN_EN_COURS;
    }

    public AdnInventoryStub(boolean commandeAdnEnCours) {
        this.commandeAdnEnCours = commandeAdnEnCours;
    }

    @Override
    public boolean isCommandeAdnEnCours(Integer id) throws GeniteurException {
        return commandeAdnEnCours;
    }
    
}
