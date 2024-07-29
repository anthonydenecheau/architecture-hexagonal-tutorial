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
    public Race byId(Integer id) throws GeniteurException {
        Race race = null;
        String sql = "SELECT r.IDENT_RRACE " +
            " , TO_CHAR(r.DATE_DEROGATION_ADN,'DD/MM/YYYY') DATE_DEROGATION_ADN " +
            " , r.NB_AGE_MINI_CONFIRMATION " +
            " FROM RRACE r " +
            " WHERE r.IDENT_RRACE = ? "
            ;
        try {
            race = jdbcTemplate.queryForObject(sql, new RaceMapper(), new Object[]{(Object) id});
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucune race trouvée pour le chien : " + id);
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [byId] : " + e.getMessage());
        }
        return race;
    }

    public Race Step6_byGeniteurId(Integer id) throws GeniteurException {
        return new Race(56, null, 12);
    }

}
