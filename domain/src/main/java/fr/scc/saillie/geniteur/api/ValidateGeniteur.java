package fr.scc.saillie.geniteur.api;

import java.time.LocalDate;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;

/**
 * Api - Validation d'un geniteur
 *
 * @author anthonydenecheau
 */
public interface ValidateGeniteur {

    /** 
     * Valide l'ensemble des règles pour un geniteur lors d'une saillie
     * @param dateSaillie date de saillie
     * @param geniteur informations geniteur
     * @return String
     * @throws GeniteurException dans le cas d'une non conformité
     */    
    String execute(LocalDate dateSaillie, Geniteur geniteur) throws GeniteurException;
}
