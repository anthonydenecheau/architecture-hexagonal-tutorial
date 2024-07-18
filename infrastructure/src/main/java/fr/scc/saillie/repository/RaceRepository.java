package fr.scc.saillie.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Race;
import fr.scc.saillie.geniteur.spi.RaceInventory;
import fr.scc.saillie.mapper.RaceMapper;

@Component
public class RaceRepository implements RaceInventory {

    final JdbcTemplate jdbcTemplate;

    public RaceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
   
    @Override
    public Race byGeniteurId(Integer id) throws GeniteurException {
        Race race = null;
        String sql = "SELECT r.IDENT_RRACE " +
            " , r.NB_AGE_MINI_CONFIRMATION " +
            " FROM RRACE r, RCHIEN c " +
            " WHERE r.IDENT_RRACE = c.IDENT_RRACE " +
            " AND c.IDENT_RCHIEN = ? "
            ;
        try {
            race = jdbcTemplate.queryForObject(sql, new RaceMapper(), new Object[]{(Object) id});
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucun chien trouvé avec l'identifiant : " + id, null);
        } catch (Exception e) {
            System.out.println("Error byIdentifiant : " + e.getMessage());
            return null;
        }
        return race;
        //return new Race(56, 12);
    }

}
