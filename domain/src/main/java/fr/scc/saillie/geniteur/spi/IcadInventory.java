package fr.scc.saillie.geniteur.spi;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;

/**
 * Spi - Classe IcadInventory
 *
 * @author anthonydenecheau
 */
public interface IcadInventory {

     /** 
     * Recherche des informations du géniteur chez ICad
     * @param id geniteur
     * @return Geniteur
     * @throws GeniteurException dans le cas d'un problème de lecture
     */    
    Geniteur byIdentifiant(String tatouage, String puce) throws GeniteurException;

}
