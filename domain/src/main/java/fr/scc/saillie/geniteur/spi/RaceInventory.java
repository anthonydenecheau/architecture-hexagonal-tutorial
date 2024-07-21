package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;

/**
 * Spi - Classe RaceInventory
 *
 * @author anthonydenecheau
 */
public interface RaceInventory {

    /** 
     * Recherche des informations de la race du géniteur
     * @param id race
     * @return Race
     * @throws GeniteurException dans le cas d'un problème de lecture
     */    
    Race byId(Integer id) throws GeniteurException;
}
