package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;

/**
 * Spi - Classe PersonneInventory
 *
 * @author anthonydenecheau
 */
public interface PersonneInventory {

    /** 
     * Recherche des informations d'un éleveur ou d'un propriétaire
     * @param id (éleveur == idEleveur /propriétaire == idChien)
     * @return Personne
     * @throws GeniteurException dans le cas d'un problème de lecture
     */    
    Personne byId(Integer id, PROFIL profil) throws GeniteurException;
}
