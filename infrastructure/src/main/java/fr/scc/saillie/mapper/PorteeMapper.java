package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Portee;
import fr.scc.saillie.geniteur.model.TYPE_STATUT_DOSSIER;

@Component
public class PorteeMapper implements RowMapper<Portee> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    @Override
    @Nullable
    public Portee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Portee (
            rs.getInt("NUM_DOSSIER_SAILLIE")
            , TYPE_STATUT_DOSSIER.valueOf(rs.getString("STATUT"))
            , ConvertStringToLocalDate(rs.getString("DATE_SAILLIE"))
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
