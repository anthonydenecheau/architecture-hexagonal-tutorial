package fr.scc.saillie;

import java.time.LocalDate;

import fr.scc.ddd.DomainService;
import fr.scc.saillie.api.ValidateGeniteur;
import fr.scc.saillie.error.GeniteurException;
import fr.scc.saillie.model.Geniteur;

@DomainService
public class GeniteurUseCase implements ValidateGeniteur {

    @Override
    public String execute(LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException {
        if (geniteur.isValidDateNaissance(dateSaillie))
            return "Le géniteur est validé";
        else 
            throw new GeniteurException("le géniteur est née avant la saillie",null);
    }
}
