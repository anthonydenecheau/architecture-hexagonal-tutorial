package fr.scc.saillie.geniteur.api;

import java.time.LocalDate;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;

public interface ValidateGeniteur {
    String execute(LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException;
}
