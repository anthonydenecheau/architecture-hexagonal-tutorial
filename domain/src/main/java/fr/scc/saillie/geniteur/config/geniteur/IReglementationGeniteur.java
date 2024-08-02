package fr.scc.saillie.geniteur.config.geniteur;

import java.time.LocalDate;
import java.util.List;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.spi.AdnInventory;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;
import fr.scc.saillie.geniteur.spi.IcadInventory;
import fr.scc.saillie.geniteur.spi.PersonneInventory;
import fr.scc.saillie.geniteur.spi.RaceInventory;

/**
 * IReglementationGeniteur
 *
 * @author anthonydenecheau
 */
public interface IReglementationGeniteur {

    List<Message> execute(int idEleveur, LocalDate dateSaillie, Geniteur geniteur, PersonneInventory personneInventory, GeniteurInventory geniteurInventory, RaceInventory raceInventory, AdnInventory adnInventory, IcadInventory icadInventory) throws GeniteurException;;
}
