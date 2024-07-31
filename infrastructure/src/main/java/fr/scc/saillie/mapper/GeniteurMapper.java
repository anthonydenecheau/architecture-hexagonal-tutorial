package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Confirmation;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;
import fr.scc.saillie.geniteur.utils.DateUtils;

@Component
public class GeniteurMapper implements RowMapper<Geniteur> {

    @Override
    @Nullable
    public Geniteur mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Geniteur(
            rs.getInt("IDENT_RCHIEN")
            , rs.getInt("IDENT_RRACE")
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_NAISSANCE"))
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_DECES"))
            , TYPE_INSCRIPTION.valueOf(rs.getString("TYPE_INSCRIPTION"))
            , SEXE.valueOf(rs.getString("SEXE"))
            , new Confirmation(
                rs.getInt("NUM_DOSSIER_CONFIRMATION")
                , rs.getInt("NUM_CONFIRMATION")
                , DateUtils.convertStringToLocalDate(rs.getString("DATE_CONFIRMATION"))
                , (rs.getString("ON_APTE_CONFIRMATION").equals("O") ? true : false)
                , (rs.getString("ON_APPEL_ENCOURS_CONF").equals("O") ? true : false)
                , (rs.getString("ON_AJOURNE_CONFIRMATION").equals("O") ? true : false))
            , null
            , null
            , true
            , true
        );
    }
}
