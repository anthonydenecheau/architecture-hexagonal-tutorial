package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Litige;

@Component
public class LitigeMapper  implements RowMapper<Litige> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Override
    @Nullable
    public Litige mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Litige(
            rs.getString("MOTIF")
            , ConvertStringToLocalDate(rs.getString("DATE_OUVERTURE"))
            , ConvertStringToLocalDate(rs.getString("DATE_FERMETURE"))
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
