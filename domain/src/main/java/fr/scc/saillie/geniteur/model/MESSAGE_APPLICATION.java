package fr.scc.saillie.geniteur.model;

import java.util.HashMap;
import java.util.Map;

public enum MESSAGE_APPLICATION {

    VALIDE("01","le géniteur est validé"),
    ELEVEUR_LITIGE("971","l'éleveur a un litige"),
    GENITEUR_SEXE("910","le géniteur n'est pas du bon sexe"),
    GENITEUR_DECES("940","la lice est déclarée morte à la date de saillie"),
    GENITEUR_MAX_PORTEES("977","la lice a déjà fait 8 portées avec des chiots inscrits au LOF"),
    GENITEUR_ALERTE_PORTEES("978","la portée sera la 8ème portée, ce sera donc la dernière portée pour la lice"),
    GENITEUR_PROVISOIRE("950","le géniteur est inscrit à titre provisoire"),
    GENITEUR_DATE_NAISSANCE("930","le géniteur est née après la saillie"),
    GENITEUR_TROP_JEUNE("920","le géniteur n'est pas en âge de reproduire"),
    GENITEUR_TROP_AGE("960","la lice est trop âgée pour reproduire"),
    GENITEUR_DELAI("975","une saillie a déjà eu lieu lors des 5 derniers mois pour cette lice"),
    PROPRIETAIRE_LITIGE("976","le propriétaire du géniteur a un litige"),
    GENITEUR_LITIGE("972","le géniteur possède des litiges"),
    GENITEUR_APPEL_CONFIRMATION("973","le géniteur a un appel sur la confirmation"),
    GENITEUR_INAPTE_CONFIRMATION("974","le géniteur a été ajourné ou déclaré inapte à la confirmation"),
    GENITEUR_NON_CONFIRME("970","le géniteur n'est pas confirmé"),
    GENITEUR_GENEALOGIE("979","la généalogie du géniteur n'est pas complète sur 3 générations"),
    GENITEUR_EMPREINTE("980","l'empreinte ADN du géniteur n'est pas enregistrée"),
    ;
    
    private static final Map<String, MESSAGE_APPLICATION> BY_CODE = new HashMap<>();
    private static final Map<String, MESSAGE_APPLICATION> BY_MESSAGE = new HashMap<>();

    static {
        for (MESSAGE_APPLICATION e : values()) {
            BY_CODE.put(e.code, e);
            BY_MESSAGE.put(e.message, e);
        }
    }

    public final String code;
    public final String message;

    private MESSAGE_APPLICATION(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MESSAGE_APPLICATION valueOfCode(String code) {
        return BY_CODE.get(code);
    }

    public static MESSAGE_APPLICATION valueOfMessage(String message) {
        return BY_MESSAGE.get(message);
    }

}
