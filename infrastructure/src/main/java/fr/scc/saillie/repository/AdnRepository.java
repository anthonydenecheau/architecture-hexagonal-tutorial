package fr.scc.saillie.repository;

import java.sql.SQLException;
import java.util.Map;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.spi.AdnInventory;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Types;

@Component
public class AdnRepository implements AdnInventory {

    final JdbcTemplate jdbcTemplate;

    public AdnRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isCommandeAdnEnCours(Integer id)  throws GeniteurException {

        boolean result = false;
        try {

            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("PKG_ADN_COMMANDE")
                    .withProcedureName("CONTROLE_SAILLIE_GENITEUR")
                    .withoutProcedureColumnMetaDataAccess()
                    .useInParameterNames("PN_IdChien")
                    .useInParameterNames("PC_OnSexe")
                    .declareParameters(
                            new SqlParameter("PN_IdChien", Types.INTEGER),
                            new SqlParameter("PC_OnSexe", Types.VARCHAR),
                            new SqlInOutParameter("PT_Message", Types.ARRAY, "TYPE_TAB_MESSAGE"));

            ARRAY p_messages = null;
            try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
                OracleConnection oc = conn.unwrap(OracleConnection.class);
                p_messages = (ARRAY) oc.createOracleArray("TYPE_TAB_MESSAGE", new Object[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            final MapSqlParameterSource paramMap = new MapSqlParameterSource()
                    .addValue("PN_IdChien", id)
                    .addValue("PC_OnSexe", "Dummy")
                    .addValue("PT_Message", p_messages);

            final Map map = simpleJdbcCall.execute(paramMap);
            final Array datas = (Array) map.get("PT_Message");
            if (datas != null) {
                for (Object message : (Object[]) datas.getArray()) {
                    STRUCT obj = (STRUCT) message;
                    String codeMessage = (String) obj.getAttributes()[2];
                    if ("SA017".equals(codeMessage) || "SA018".equals(codeMessage))
                        result = true;
                }
            }
            return result;

        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [isCommandeAdnEnCours] : " + e.getMessage());
        }
    }


}
