package fr.scc.saillie.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Portee;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;
import fr.scc.saillie.mapper.GeniteurMapper;
import fr.scc.saillie.mapper.LitigeMapper;
import fr.scc.saillie.mapper.PorteeMapper;

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
            " , TO_CHAR(c.DATE_DECES,'DD/MM/YYYY') DATE_DECES " +
            " , y.LIBELLE TYPE_INSCRIPTION " +            
            " , DECODE(c.ON_SEXE_MALE,'O','MALE','FEMELLE') SEXE " +
            " , c.NUM_DOSSIER_CONFIRMATION " +
            " , c.NUM_CONFIRMATION " +
            " , TO_CHAR(c.DATE_CONFIRMATION,'DD/MM/YYYY') DATE_CONFIRMATION " +
            " , c.ON_APTE_CONFIRMATION " +
            " , c.ON_APPEL_ENCOURS_CONF " +
            " , c.ON_AJOURNE_CONFIRMATION " +
            " FROM RCHIEN c, TYP_DEMANDE_INSCRI_LOF y " +
            " WHERE c.IDENT_RCHIEN = ? " +            
            " AND y.IDENT_TYP_DEMANDE_INSCRI_LOF = c.IDENT_TYP_DEMANDE_INSCRI_LOF "
            ;
        try {
            geniteur = jdbcTemplate.queryForObject(sql, new GeniteurMapper(), new Object[]{(Object) id});
            geniteur.withPortees(lirePorteesById(id));
            
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucun géniteur trouvé pour le chien : " + id);
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [byId] : " + e.getMessage());
        }
        return geniteur;

    }

    private List<Portee> lirePorteesById(Integer id) throws GeniteurException {
        List<Portee> portees = new ArrayList<Portee>();

        String sql = "SELECT NUM_DOSSIER_SAILLIE " +
            " , TO_CHAR(DATE_SAILLIE,'DD/MM/YYYY') DATE_SAILLIE " +
            " , CASE IDENT_TYP_STATUTDOSSIERSAILLIE " +
            "       WHEN 582 THEN 'DS_SAISIE' " +
            "       WHEN 582 THEN 'DS_SAISIE' " +
            "       WHEN 584 THEN 'DI_SAISIE' " +
            "       WHEN 10046 THEN 'DS_FORCLOS' " +
            "       ELSE '' " +
            " END AS STATUT " +
            " FROM RSAILLIE " +
            " WHERE IDENT_RCHIEN_MERE = ? "
        ;
        try {
            portees = jdbcTemplate.query(sql, new PorteeMapper(), new Object[]{(Object) id});
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [lirePorteesById] : " + e.getMessage());
        }        
        return portees;
    }

}
