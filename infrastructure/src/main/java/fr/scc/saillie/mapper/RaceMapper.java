package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Race;
import fr.scc.saillie.geniteur.utils.DateUtils;

@Component
public class RaceMapper  implements RowMapper<Race> {

    @Override
    @Nullable
    public Race mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Race(
            rs.getInt("IDENT_RRACE")
            , rs.getString("NOM_RACE_FRANCAIS")
            , DateUtils.convertStringToLocalDate(rs.getString("DATE_DEROGATION_ADN"))
            , rs.getInt("NB_AGE_MINI_CONFIRMATION")
        );
    }

}
