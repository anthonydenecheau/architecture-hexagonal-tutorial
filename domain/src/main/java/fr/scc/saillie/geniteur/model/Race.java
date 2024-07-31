package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;

/**
 * Classe Race
 *
 * @author anthonydenecheau
 */
public record Race(Integer id, String nom, LocalDate dateDerogationAdn, Integer ageMinimum) {

}
