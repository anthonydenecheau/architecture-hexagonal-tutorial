package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;

@Component
public class GeniteurMapper implements RowMapper<Geniteur> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Override
    @Nullable
    public Geniteur mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Geniteur(
            rs.getInt("IDENT_RCHIEN")
            , rs.getInt("IDENT_RRACE")
            , ConvertStringToLocalDate(rs.getString("DATE_NAISSANCE"))
            , ConvertStringToLocalDate(rs.getString("DATE_DECES"))
            , TYPE_INSCRIPTION.valueOf(rs.getString("TYPE_INSCRIPTION"))
            , SEXE.valueOf(rs.getString("SEXE"))
            , null
        );
    }

    private LocalDate ConvertStringToLocalDate(String val) {
        try {
            return LocalDate.parse(val, formatter);
        } catch (Exception e) {
            return null;
        }
    }

}
