package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Race;

@Component
public class RaceMapper  implements RowMapper<Race> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Override
    @Nullable
    public Race mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Race(
            rs.getInt("IDENT_RRACE")
            , ConvertStringToLocalDate(rs.getString("DATE_DEROGATION_ADN"))
            , rs.getInt("NB_AGE_MINI_CONFIRMATION")
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
