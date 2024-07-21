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

@Component
public class GeniteurMapper implements RowMapper<Geniteur> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Override
    @Nullable
    public Geniteur mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Geniteur(
            rs.getInt("IDENT_RCHIEN")
            , rs.getInt("IDENT_RRACE")
            , LocalDate.parse(rs.getString("DATE_NAISSANCE"), formatter)
            , SEXE.valueOf(rs.getString("SEXE"))
        );
    }

}
