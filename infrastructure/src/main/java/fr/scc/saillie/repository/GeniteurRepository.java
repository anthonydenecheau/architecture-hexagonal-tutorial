package fr.scc.saillie.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;
import fr.scc.saillie.mapper.GeniteurMapper;

@Component
public class GeniteurRepository implements GeniteurInventory {

    final JdbcTemplate jdbcTemplate;

    public GeniteurRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Geniteur byId(Integer id) throws GeniteurException {

        Geniteur geniteur = null;
        String sql = "SELECT c.IDENT_RCHIEN " +
            " , c.IDENT_RRACE " +
            " , TO_CHAR(c.DATE_NAISSANCE,'DD/MM/YYYY') DATE_NAISSANCE " +
            " , DECODE(c.ON_SEXE_MALE,'O','MALE','FEMELLE') SEXE " +
            " FROM RCHIEN c " +
            " WHERE c.IDENT_RCHIEN = ? "
            ;
        try {
            geniteur = jdbcTemplate.queryForObject(sql, new GeniteurMapper(), new Object[]{(Object) id});
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucun géniteur trouvé pour le chien : " + id);
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [byId] : " + e.getMessage());
        }
        return geniteur;

    }

}
