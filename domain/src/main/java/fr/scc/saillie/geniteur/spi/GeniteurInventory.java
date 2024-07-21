package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;

/**
 * Spi - Classe GeniteurInventory
 *
 * @author anthonydenecheau
 */
public interface GeniteurInventory {

    /** 
     * Recherche des informations du géniteur
     * @param id geniteur
     * @return Geniteur
     * @throws GeniteurException dans le cas d'un problème de lecture
     */    
    Geniteur byId(Integer id) throws GeniteurException;
}
