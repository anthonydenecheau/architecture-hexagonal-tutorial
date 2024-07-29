package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;

/**
 * Spi - Classe AdnInventory
 *
 * @author anthonydenecheau
 */
public interface AdnInventory {

    /** 
     * Recherche d'une commande ADN en cours pour le géniteur
     * @param id geniteur
     * @return boolean
     * @throws GeniteurException dans le cas d'un problème de lecture
     */    
    boolean isCommandeAdnEnCours(Integer id) throws GeniteurException;
}
