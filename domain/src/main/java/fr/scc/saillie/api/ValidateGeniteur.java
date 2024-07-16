package fr.scc.saillie.api;

import java.time.LocalDate;

import fr.scc.saillie.model.Geniteur;

public interface ValidateGeniteur {
    String execute(LocalDate dateSaillie, Geniteur geniteur);
}
