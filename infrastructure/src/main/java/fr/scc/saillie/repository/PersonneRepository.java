package fr.scc.saillie.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.PROFIL;
import fr.scc.saillie.geniteur.model.Personne;
import fr.scc.saillie.geniteur.spi.PersonneInventory;
import fr.scc.saillie.mapper.PersonneMapper;
import fr.scc.saillie.mapper.RaceMapper;

@Component
public class PersonneRepository implements PersonneInventory {

    final JdbcTemplate jdbcTemplate;

    public PersonneRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
   
    @Override
    public Personne byId(Integer id, PROFIL profil) throws GeniteurException {
        Personne personne = null;
        
        String sqlEleveur = "SELECT p.IDENT_RPERSONNE ID " +
            " , p.XCODE_POSTAL CP " +
            " , y.LIBELLE PAYS " +
            " FROM RPERSONNE p, RELEV e, TYP_PAYS y " +
            " WHERE e.IDENT_RELEV = ? " +
            " AND e.IDENT_RPERSONNE = p.IDENT_RPERSONNE " +
            " AND p.IDENT_TYP_PAYS = y.IDENT_TYP_PAYS " 
            ;
        String sqlProprietaire = "SELECT p.IDENT_RPERSONNE ID " +
            " , p.XCODE_POSTAL CP " +
            " , y.LIBELLE PAYS " +
            " FROM RPERSONNE p, RPROPRIO o, TYP_PAYS y, RPROPRIO_CHIEN h " +
            " WHERE h.IDENT_RCHIEN = ? " +
            " AND o.IDENT_RPROPRIO = h.IDENT_RPROPRIO " +
            " AND o.IDENT_RPERSONNE = p.IDENT_RPERSONNE " +
            " AND p.IDENT_TYP_PAYS = y.IDENT_TYP_PAYS " 
            ;


        try {
            if (profil.equals(PROFIL.ELEVEUR))
                personne = jdbcTemplate.queryForObject(sqlEleveur, new PersonneMapper(), new Object[]{(Object) id});
        
            if (profil.equals(PROFIL.PROPRIETAIRE))
                personne = jdbcTemplate.queryForObject(sqlProprietaire, new PersonneMapper(), new Object[]{(Object) id});
            
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucune personne trouvée pour : " + id);
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [byId] : " + e.getMessage());
        }
        return personne;
    }

}
