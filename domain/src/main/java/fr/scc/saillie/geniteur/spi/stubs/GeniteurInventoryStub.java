package fr.scc.saillie.geniteur.spi.stubs;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Confirmation;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;

@Stub
public class GeniteurInventoryStub implements GeniteurInventory {

    private static final Geniteur DEFAULT_GENITEUR;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    
    static {
        try {
            DEFAULT_GENITEUR = initialiserGeniteur();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private final Geniteur geniteur;

    public GeniteurInventoryStub() {
        geniteur = DEFAULT_GENITEUR;
    }

    public GeniteurInventoryStub(Geniteur geniteur) {
        this.geniteur = geniteur;
    }

    private static Geniteur initialiserGeniteur() throws ParseException {
        return new Geniteur(
            1
            , 56, 
            LocalDate.parse("01/01/2022", formatter), 
            null, 
            TYPE_INSCRIPTION.DESCENDANCE, 
            SEXE.FEMELLE, 
            new Confirmation(202000001, 1235, LocalDate.parse("01/02/2023", formatter), true, false, false)
        );
    }

    @Override
    public Geniteur byId(Integer id) throws GeniteurException {
        return geniteur;
    }

}
