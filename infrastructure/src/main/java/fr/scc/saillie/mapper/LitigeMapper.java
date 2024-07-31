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
import fr.scc.saillie.geniteur.utils.DateUtils;

@Component
public class LitigeMapper  implements RowMapper<Litige> {

    @Override
    @Nullable
    public Litige mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Litige(
            rs.getString("MOTIF")
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_OUVERTURE"))
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_FERMETURE"))
        );
    }

}
