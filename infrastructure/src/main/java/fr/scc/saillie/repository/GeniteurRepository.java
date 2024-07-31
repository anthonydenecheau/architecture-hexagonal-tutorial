package fr.scc.saillie.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import fr.scc.saillie.dto.Genealogie;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Litige;
import fr.scc.saillie.geniteur.model.Portee;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;
import fr.scc.saillie.geniteur.spi.GeniteurInventory;
import fr.scc.saillie.mapper.GeniteurMapper;
import fr.scc.saillie.mapper.LitigeMapper;
import fr.scc.saillie.mapper.PorteeMapper;

@Component
public class GeniteurRepository implements GeniteurInventory {

    final JdbcTemplate jdbcTemplate;

    public GeniteurRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Geniteur byId(Integer id) throws GeniteurException {

        Geniteur geniteur = null;
        String sql = "SELECT c.IDENT_RCHIEN " +
            " , c.IDENT_RRACE " +
            " , TO_CHAR(c.DATE_NAISSANCE,'DD/MM/YYYY') DATE_NAISSANCE " +
            " , TO_CHAR(c.DATE_DECES,'DD/MM/YYYY') DATE_DECES " +
            " , CASE c.IDENT_TYP_DEMANDE_INSCRI_LOF " +
            "       WHEN 537 THEN 'DESCENDANCE' " +
            "       WHEN 761 THEN 'LIVRE_ATTENTE' " +
            "       WHEN 540 THEN 'ETRANGER' " +
            "       WHEN 539 THEN 'A_TITRE_INITIAL' " +
            "       WHEN 890 THEN 'PROVISOIRE' " +
            "       ELSE '' " +
            " END AS TYPE_INSCRIPTION " +
            " , DECODE(c.ON_SEXE_MALE,'O','MALE','FEMELLE') SEXE " +
            " , c.NUM_DOSSIER_CONFIRMATION " +
            " , c.NUM_CONFIRMATION " +
            " , TO_CHAR(c.DATE_CONFIRMATION,'DD/MM/YYYY') DATE_CONFIRMATION " +
            " , c.ON_APTE_CONFIRMATION " +
            " , c.ON_APPEL_ENCOURS_CONF " +
            " , c.ON_AJOURNE_CONFIRMATION " +
            " FROM RCHIEN c " +
            " WHERE c.IDENT_RCHIEN = ? "            
            ;
        try {
            geniteur = jdbcTemplate.queryForObject(sql, new GeniteurMapper(), new Object[]{(Object) id});
            geniteur.withPortees(lirePorteesById(id));
            geniteur.withLitiges(lireLitigesById(id));
            geniteur.setGenealogieComplete(lireGenealogieById(id));
            geniteur.setEmpreinteAdn(lireEmpreinteAdnById(id));
            
        } catch (EmptyResultDataAccessException e) {
            throw new GeniteurException("Aucun géniteur trouvé pour le chien : " + id);
        } catch (Exception e) {
            throw new GeniteurException("GeniteurRepository Erreur technique [byId] : " + e.getMessage());
        }
        return geniteur;

    }

    private List<Portee> lirePorteesById(Integer id) throws GeniteurException {
        List<Portee> portees = new ArrayList<Portee>();

        String sql = "SELECT NUM_DOSSIER_SAILLIE " +
            " , TO_CHAR(DATE_SAILLIE,'DD/MM/YYYY') DATE_SAILLIE " +
            " , CASE IDENT_TYP_STATUTDOSSIERSAILLIE " +
            "       WHEN 582 THEN 'DS_SAISIE' " +
            "       WHEN 582 THEN 'DS_SAISIE' " +
            "       WHEN 584 THEN 'DI_SAISIE' " +
            "       WHEN 10046 THEN 'DS_FORCLOS' " +
            "       ELSE '' " +
            " END AS STATUT " +
            " FROM RSAILLIE " +
            " WHERE IDENT_RCHIEN_MERE = ? "
        ;
        try {
            portees = jdbcTemplate.query(sql, new PorteeMapper(), new Object[]{(Object) id});
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [lirePorteesById] : " + e.getMessage());
        }        
        return portees;
    }

    private List<Litige> lireLitigesById(Integer id) throws GeniteurException {
        List<Litige> litiges = new ArrayList<Litige>();

        String sql = "SELECT IDENT_TYP_LITIGE_CHIEN MOTIF " +
            " , TO_CHAR(DATE_OUVERTURE,'DD/MM/YYYY') DATE_OUVERTURE " +
            " , TO_CHAR(DATE_FERMETURE,'DD/MM/YYYY') DATE_FERMETURE " +
            " FROM RCHIEN_LITIGE " +
            " WHERE IDENT_RCHIEN = ? "
        ;
        try {
            litiges = jdbcTemplate.query(sql, new LitigeMapper(), new Object[]{(Object) id});
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [lireLitigesById] : " + e.getMessage());
        }        
        return litiges;
    }

    public boolean lireEmpreinteAdnById(Integer id) throws GeniteurException {
        // Attention: l'empreinte doit être valide 
        return ("O".equals(lireEmpreinteIsag2006(id)) || "O".equals(lireEmpreinteIsag2020(id)));
    }

    private String lireEmpreinteIsag2006(Integer id) throws GeniteurException {

        String sql = "SELECT ON_FILIATION " +
            " FROM RADN_EMPREINTE " +
            " WHERE ON_ACTIF = 'O' " +
            " AND IDENT_RCHIEN = ? "
        ;
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return "";
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [lireEmpreinteIsag2006] : " + e.getMessage());
        }
    }

    private String lireEmpreinteIsag2020(Integer id) throws GeniteurException {

        String sql = "SELECT ON_FILIATION " +
            " FROM RADN_EMPREINTE_SNP_P1 " +
            " WHERE ON_ACTIF = 'O' " +
            " AND IDENT_RCHIEN = ? "
        ;
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return "";
        } catch (Exception e) {
            throw new GeniteurException("Erreur technique [lireEmpreinteIsag2020] : " + e.getMessage());
        }
    }

    public boolean lireGenealogieById(Integer id) throws GeniteurException {
        Genealogie genealogie = new Genealogie(id, null);
        initialiserGenealogie(genealogie, 1);
        return genealogie != null && genealogie.getPere() != null && genealogie.getMere() != null && genealogie.getPere().getPere() != null && genealogie.getPere().getMere() != null && genealogie.getMere().getPere() != null && genealogie.getMere().getMere() != null;
    }

    private void initialiserGenealogie(Genealogie sujet, int level) {

        int idSujet = sujet.getId();
        Genealogie pere = null;
        Genealogie mere = null;

        String sql = "SELECT NOM_CHIEN " +
            " , c.IDENT_RRACE " +
            " , CASE c.IDENT_TYP_DEMANDE_INSCRI_LOF " +
            "       WHEN 537 THEN 'DESCENDANCE' " +
            "       WHEN 761 THEN 'LIVRE_ATTENTE' " +
            "       WHEN 540 THEN 'ETRANGER' " +
            "       WHEN 539 THEN 'A_TITRE_INITIAL' " +
            "       WHEN 890 THEN 'PROVISOIRE' " +
            "       ELSE '' " +
            " END AS TYPE_INSCRIPTION " +
            " , c.IDENT_RRACE, ON_SEXE_MALE " +
            " , c.IDENT_RCHIEN_PERE " +
            " , c.IDENT_RCHIEN_MERE " +
            " FROM RCHIEN c " + 
            " WHERE c.IDENT_RCHIEN = ? "
        ;
        List<Genealogie> geniteurs = (List<Genealogie>) jdbcTemplate.query(
                sql, new ResultSetExtractor<List<Genealogie>>() {
                    @Override
                    public List<Genealogie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Genealogie> geniteurs = new ArrayList<>();
                        int idPere = 0;
                        int idMere = 0;
                        while (rs.next()) {
                            String nom = rs.getString("NOM_CHIEN");
                            TYPE_INSCRIPTION typ_inscription = TYPE_INSCRIPTION.valueOf(rs.getString("TYPE_INSCRIPTION"));
                            String sexe = rs.getString("ON_SEXE_MALE");
                            Integer idRace = rs.getInt("IDENT_RRACE");
                            // Cas particulier de la généalogie
                            if ("CHIEN NON INSCRIT AUX LIVRES DES ORIGINES".equals(nom)
                                || TYPE_INSCRIPTION.A_TITRE_INITIAL.equals(typ_inscription)
                                || TYPE_INSCRIPTION.LIVRE_ATTENTE.equals(typ_inscription)
                                ) {
                                // On simule le reste de la généalogie comme complète
                                // On va rechercher pour la race l'étalon/la lice 
                                // On doit avoir déjà ici l'un des deux !
                                if ("O".equals(sexe)){
                                    idPere = idSujet;
                                    // lire l'équivalent pour la femelle
                                    idMere = lireChienNonInscrit(SEXE.FEMELLE,idRace);
                                }
                                if ("N".equals(sexe)){
                                    idPere = lireChienNonInscrit(SEXE.MALE,idRace);
                                    idMere = idSujet;
                                }
                            } else {
                                idPere = rs.getInt("IDENT_RCHIEN_PERE");
                                idMere = rs.getInt("IDENT_RCHIEN_MERE");
                            }

                            if (idPere != 0)
                                geniteurs.add(new Genealogie(idPere, SEXE.MALE));

                            if (idMere != 0)
                                geniteurs.add(new Genealogie(idMere, SEXE.FEMELLE));

                        }
                        return geniteurs;
                    }
                }, new Object[]{(Object) idSujet});

        pere = geniteurs.stream().filter(g -> g.sexe == SEXE.MALE).findFirst().orElse(null);
        if (pere != null) {
            pere.sexe = SEXE.MALE;
            sujet.setPere(pere);
            if (level <= 3) {
                initialiserGenealogie(pere, level + 1);
            }
        }
        mere = geniteurs.stream().filter(g -> g.sexe == SEXE.FEMELLE).findFirst().orElse(null);
        if (mere != null) {
            mere.sexe = SEXE.FEMELLE;
            sujet.setMere(mere);
            if (level <= 3) {
                initialiserGenealogie(mere, level + 1);
            }
        }
    }    

    private int lireChienNonInscrit(SEXE sexe, Integer idRace) {
        String sql = "SELECT IDENT_RCHIEN " +
            " FROM RCHIEN " +
            " WHERE NOM_CHIEN = 'CHIEN NON INSCRIT AUX LIVRES DES ORIGINES' " +
            " AND IDENT_RRACE = ? " +
            " AND ON_SEXE_MALE = ? "
        ;
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, idRace, (sexe.equals(SEXE.MALE) ? "O" : "N"));
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Chien non trouvé [lireChienNonInscrit] : ["+sexe+"] ["+idRace+"]");
            return 0;
        } catch (Exception e) {
            System.out.println("Erreur technique [lireChienNonInscrit] : " + e.getMessage());
            return 0;
        }
    }

}
