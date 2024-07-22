package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;

public record Confirmation(int numDossier, int numConfirmation, LocalDate dateConfirmation, boolean isConfirme, boolean isOnAppel, boolean isAjourne) {

}
