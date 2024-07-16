package fr.scc.saillie.model;

import java.time.LocalDate;

public record Geniteur(int id, LocalDate dateNaissance) {

    public boolean isValidDateNaissance (LocalDate dateSaillie) {
        return this.dateNaissance.isBefore(dateSaillie);
    }
}
