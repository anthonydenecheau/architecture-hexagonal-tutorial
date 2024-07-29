package fr.scc.saillie.geniteur.model;

import java.time.LocalDate;

public record Portee (int numDossier, TYPE_STATUT_DOSSIER statut, LocalDate dateSaillie) {

    public boolean isEligibleForControle() {
        return TYPE_STATUT_DOSSIER.DS_SAISIE.equals(statut)
            || TYPE_STATUT_DOSSIER.DN_SAISIE.equals(statut)
            || TYPE_STATUT_DOSSIER.DI_SAISIE.equals(statut)
            || TYPE_STATUT_DOSSIER.DS_EN_ATTENTE_DE_PAIEMENT.equals(statut)
            || TYPE_STATUT_DOSSIER.DI_EN_ATTENTE_DE_PAIEMENT.equals(statut)
        ;
    }
}
