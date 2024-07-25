package fr.scc.saillie.geniteur.spi.stubs;

import java.text.ParseException;

import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Litige;
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;
import fr.scc.saillie.geniteur.spi.PersonneInventory;

@Stub
public class PersonneInventoryStub implements PersonneInventory {

    private static final Personne DEFAULT_PERSONNE;

    static {
        try {
            DEFAULT_PERSONNE = initialiserPersonne();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private final Personne personne;

    public PersonneInventoryStub() {
        personne = DEFAULT_PERSONNE;
    }

    public PersonneInventoryStub(Personne personne) {
        this.personne = personne;
    }

    private static Personne initialiserPersonne() throws ParseException {
        return new Personne(1,"44", "FRANCE", null);
    }

    @Override
    public Personne byId(Integer id, PROFIL profil) throws GeniteurException {
        return personne;
    }
    
}
