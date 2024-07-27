package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe Personne (eleveur / propriétaire)
 *
 * @author anthonydenecheau
 */
public class Personne {

    int id;
    String departement;
    String pays;
    List<Litige> litiges;

    public Personne(int id, String departement, String pays) {
        this.id = id;
        this.departement = departement;
        this.pays = pays;
    }

    public Personne(int id, String departement, String pays, List<Litige> litiges) {
        this.id = id;
        this.departement = departement;
        this.pays = pays;
        this.litiges = litiges;
    }

    public int getId() {
        return id;
    }
    
    public void withLitiges(List<Litige> litiges) {
        this.litiges = litiges;
    }

    /** 
     * <p>Présence de litiges à la date de saillie ou litige ouvert</p>
     * @param  dateSaillie date de saillie
     * @return boolean
     */
    public boolean hasLitige(LocalDate dateSaillie) {
        if (this.litiges == null || this.litiges.isEmpty())
            return false;

        return this.litiges.stream()
                .anyMatch(litige -> litige.dateOuverture().isBefore(dateSaillie)
                        && ( (litige.dateFermeture() == null ? true : litige.dateFermeture().isAfter(dateSaillie)) ) );

    }    

    /** 
     * <p>Lieu de résidance dans les DOMTOM</p>
     * @return boolean
     */
    public boolean isResidantDOMTOM () {
        // Note: le département 0 est un département fictif associé aux anciennes colonies
        if ( this.pays.equals("FRANCE") 
            && (this.departement.equals("0")
                || (Integer.parseInt(this.departement) >= 97 && Integer.parseInt(this.departement) >= 99 ))) {
            return true;
        }
        return this.pays.equals("GUADELOUPE")
            || this.pays.equals("MARTINIQUE")
            || this.pays.equals("REUNION")
            || this.pays.equals("POLYNESIE FRANCAISE")
            ;

    }

    /** 
     * <p>Lieu de résidance dans un pays étranger</p>
     * @return boolean
     */
    public boolean isResidantEtranger () {
        return !this.pays.equals("FRANCE") 
            && !this.pays.equals("GUADELOUPE")
            && !this.pays.equals("MARTINIQUE")
            && !this.pays.equals("REUNION")
            && !this.pays.equals("POLYNESIE FRANCAISE")
            ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((departement == null) ? 0 : departement.hashCode());
        result = prime * result + ((pays == null) ? 0 : pays.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Personne other = (Personne) obj;
        if (id != other.id)
            return false;
        if (departement == null) {
            if (other.departement != null)
                return false;
        } else if (!departement.equals(other.departement))
            return false;
        if (pays == null) {
            if (other.pays != null)
                return false;
        } else if (!pays.equals(other.pays))
            return false;
        return true;
    }
}
