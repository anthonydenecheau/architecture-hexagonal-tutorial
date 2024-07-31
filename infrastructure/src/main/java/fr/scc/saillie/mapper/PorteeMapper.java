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
import fr.scc.saillie.geniteur.utils.DateUtils;

@Component
public class PorteeMapper implements RowMapper<Portee> {

    @Override
    @Nullable
    public Portee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Portee (
            rs.getInt("NUM_DOSSIER_SAILLIE")
            , TYPE_STATUT_DOSSIER.valueOf(rs.getString("STATUT"))
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_SAILLIE"))
        );
    }

}
