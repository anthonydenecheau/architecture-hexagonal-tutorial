package fr.scc.saillie.geniteur.spi.stubs;

import static java.util.Arrays.asList;

import java.text.ParseException;

import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.IcadInventory;
import fr.scc.saillie.geniteur.utils.DateUtils;

@Stub
public class IcadInventoryStub implements IcadInventory {

    private static final Geniteur DEFAULT_GENITEUR;
    
    static {
        try {
            DEFAULT_GENITEUR = initialiserGeniteur();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private final Geniteur geniteur;

    public IcadInventoryStub() {
        geniteur = DEFAULT_GENITEUR;
    }

    public IcadInventoryStub(Geniteur geniteur) {
        this.geniteur = geniteur;
    }

    private static Geniteur initialiserGeniteur() throws ParseException {
        return new Geniteur(
            0
            , 0
            , "2DND115"
            , null
            , DateUtils.convertStringToLocalDate("01/01/2022")
            , null
            , null
            , null
            , null
            , asList()
            , asList()
            , false
            , false
        );
    }

    @Override
    public Geniteur byIdentifiant(String tatouage, String puce) throws GeniteurException {
        return geniteur;
    }

}
