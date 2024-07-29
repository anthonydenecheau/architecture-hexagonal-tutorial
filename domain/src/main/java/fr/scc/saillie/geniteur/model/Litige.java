package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;

public record Litige(String motif, LocalDate dateOuverture, LocalDate dateFermeture) {

}
