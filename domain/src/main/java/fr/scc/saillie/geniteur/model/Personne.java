package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe Personne (eleveur / propriétaire)
 *
 * @author anthonydenecheau
 */
public record Personne(int id, String departement, String pays, List<Litige> litiges) {

    public Personne(int id, String departement, String pays) {
        this(id, departement, pays, null);
    }

    public Personne withLitiges(List<Litige> litiges) {
        return new Personne(id(), departement(), pays(), litiges);
    }

    /** 
     * <p>Présence de litiges à la date de saillie ou litige ouvert</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean hasLitige(LocalDate dateSaillie) {
        if (this.litiges == null)
            return false;

        return this.litiges.stream()
                .anyMatch(litige -> litige.dateOuverture().isBefore(dateSaillie)
                        && ( litige.dateFermeture().isAfter(dateSaillie) || litige.dateFermeture() == null) );

    }    

    /** 
     * <p>Lieu de résidance dans les DOMTOM</p>
     * @return boolean
     */
    public boolean isResidantDOMTOM () {
        // Note: le département 0 est un département fictif associé aux anciennes colonies
        if ( pays().equals("FRANCE") 
            && (departement().equals("0")
                || (Integer.parseInt(departement()) >= 97 && Integer.parseInt(departement()) >= 99 ))) {
            return true;
        }
        return pays().equals("GUADELOUPE")
            || pays().equals("MARTINIQUE")
            || pays().equals("REUNION")
            || pays().equals("POLYNESIE FRANCAISE")
            ;

    }

    /** 
     * <p>Lieu de résidance dans un pays étranger</p>
     * @return boolean
     */
    public boolean isResidantEtranger () {
        return !pays().equals("FRANCE") 
            && !pays().equals("GUADELOUPE")
            && !pays().equals("MARTINIQUE")
            && !pays().equals("REUNION")
            && !pays().equals("POLYNESIE FRANCAISE")
            ;
    }
}
