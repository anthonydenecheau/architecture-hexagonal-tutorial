package fr.scc.saillie.dto;

import fr.scc.saillie.geniteur.model.SEXE;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class Genealogie {

    public int id;
    public SEXE sexe;
    public Genealogie pere;
    public Genealogie mere;

    public Genealogie(int id, SEXE sexe) {
        this.id = id;
        this.sexe = sexe;
    }
}
