package fr.scc.saillie.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.model.Personne;

@Component
public class PersonneMapper  implements RowMapper<Personne> {

    @Override
    @Nullable
    public Personne mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Personne(
            rs.getInt("ID")
            , rs.getString("CP")
            , rs.getString("PAYS")
        );
    }

}
