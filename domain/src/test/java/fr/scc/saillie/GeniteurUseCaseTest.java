package fr.scc.saillie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Arrays.asList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.scc.saillie.geniteur.GeniteurUseCase;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.model.Confirmation;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Litige;
import fr.scc.saillie.geniteur.model.MESSAGE_APPLICATION;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.model.Personne;
import fr.scc.saillie.geniteur.model.Portee;
import fr.scc.saillie.geniteur.model.Race;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.geniteur.model.TYPE_INSCRIPTION;
import fr.scc.saillie.geniteur.model.TYPE_STATUT_DOSSIER;

public class GeniteurUseCaseTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    int idEleveur = 0;
    Geniteur geniteur = null;
    Race race;
    Personne personne;    
    List<Message> messages;
    boolean commandeAdnEnCours;

    @BeforeEach
    public void setUp() {
        this.idEleveur = 1;
        Confirmation confirmation = new Confirmation(202300001, 1, LocalDate.parse("01/01/2023", formatter), true, false, false);
        this.geniteur = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, confirmation, null, null, true, true);
        this.race = new Race(56,null,12);
        this.personne = new Personne(1,"44", "FRANCE", null);
        this.commandeAdnEnCours = false;
        this.messages = new ArrayList<Message>();
    }
    
    @Test
    @DisplayName("Step1")
    void should_validate_date_naissance() {
        LocalDate dateSaillie = LocalDate.parse("01/01/2024", formatter);
        LocalDate dateNaissance = LocalDate.parse("01/01/2022", formatter);
        assertThat(dateNaissance.isBefore(dateSaillie)).isTrue();
    }

    @Test
    @DisplayName("Step2")
    void should_validate_date_naissance_geniteur() {
        LocalDate dateSaillie = LocalDate.parse("01/01/2024", formatter);
        assertThat(geniteur.isValidDateNaissance(dateSaillie)).isTrue();
    }

    @Test
    @DisplayName("Step2")
    void should_not_validate_date_naissance_geniteur() {
        LocalDate dateSaillie = LocalDate.parse("01/01/2021", formatter);
        assertThat(geniteur.isValidDateNaissance(dateSaillie)).isFalse();
    }

    @Test
    @DisplayName("Step3")
    void should_authorize_geniteur() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/01/2024", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try  {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.VALIDE.message);
    }

    @Test
    @DisplayName("Step3")
    void should_not_authorize_geniteur() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/01/2021", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_DATE_NAISSANCE.message);
    }

    @Test
    @DisplayName("Step5")
    void should_not_authorize_age_minimum() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2022", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_TROP_JEUNE.message);
    }

    @Test
    @DisplayName("Step5")
    void should_authorize_age_minimum() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.VALIDE.message);
    }

    @Test
    @DisplayName("Step8")
    void should_not_authorize_sexe() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurAnoSexe = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.MALE, null, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurAnoSexe);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_SEXE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_femelle_deces() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurAnoDeces = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), LocalDate.parse("01/07/2024", formatter), TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoDeces), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_DECES.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_type_inscription() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurAnoTypeInscription = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.PROVISOIRE, SEXE.FEMELLE, null, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoTypeInscription), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_PROVISOIRE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_lice_age_maximum() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurAnoLiceAgeMax = new Geniteur(1, 56, LocalDate.parse("01/01/2010", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoLiceAgeMax), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_TROP_AGE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_eleveur_litiges() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/05/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);

        List<Litige> litigesEleveur = asList(
            new Litige("absence de règlement", LocalDate.parse("01/01/2023", formatter), LocalDate.parse("01/06/2024", formatter)));
        Personne _personne = new Personne(1, "44", "FRANCE", litigesEleveur);

        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (_personne), (g) -> (this.geniteur), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.ELEVEUR_LITIGE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_geniteur_litiges() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/05/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        List<Litige> litiges = asList(
            new Litige("absence de règlement", LocalDate.parse("01/01/2023", formatter), LocalDate.parse("01/06/2024", formatter)));
        Geniteur geniteurAnoLitiges = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, litiges, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoLitiges), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_LITIGE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_authorize_exception_confirmation() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurNonConfirmation = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, true, true);
        Personne _personne = new Personne(1, "977", "GUADELOUPE", null);

        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (_personne), (g) -> (geniteurNonConfirmation), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.VALIDE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_appel_confirmation() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Confirmation confirmation = new Confirmation(202300001, 1, LocalDate.parse("01/01/2023", formatter), false, true, false);
        Geniteur geniteurAnoConfirmation = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, confirmation, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoConfirmation), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_APPEL_CONFIRMATION.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_ajourne_confirmation() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Confirmation confirmation = new Confirmation(202300001, 1, LocalDate.parse("01/01/2023", formatter), false, false, true);
        Geniteur geniteurAnoConfirmation = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, confirmation, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoConfirmation), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_INAPTE_CONFIRMATION.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_confirmation() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurConfirmation = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurConfirmation), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_NON_CONFIRME.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_lice_saillie() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        List<Portee> portees = asList(
            new Portee(20230003, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/05/2024", formatter))
            , new Portee(20230002, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/11/2023", formatter))
            , new Portee(20230001, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/01/2023", formatter))
        );
        Geniteur geniteurAnoSaillieLice = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, portees, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoSaillieLice), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_DELAI.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_lice_portees() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        List<Portee> portees = asList(
            new Portee(20230008, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/06/2023", formatter))
            , new Portee(20230007, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/05/2023", formatter))
            , new Portee(20230006, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/04/2023", formatter))
            , new Portee(20230005, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/03/2023", formatter))
            , new Portee(20230004, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/02/2023", formatter))
            , new Portee(20230003, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/01/2023", formatter))
            , new Portee(20230002, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/12/2022", formatter))
            , new Portee(20230001, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/11/2022", formatter))
        );
        Geniteur geniteurAnoSaillieLicePortees = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, portees, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoSaillieLicePortees), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_MAX_PORTEES.code);
    }

    @Test
    @DisplayName("Step9")
    void should_warning_lice_portees() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        List<Portee> portees = asList(
            new Portee(20230008, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/06/2023", formatter))
            , new Portee(20230007, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/05/2023", formatter))
            , new Portee(20230006, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/04/2023", formatter))
            , new Portee(20230005, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/03/2023", formatter))
            , new Portee(20230004, TYPE_STATUT_DOSSIER.DS_FORCLOS, LocalDate.parse("01/02/2023", formatter))
            , new Portee(20230003, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/01/2023", formatter))
            , new Portee(20230002, TYPE_STATUT_DOSSIER.DI_SAISIE, LocalDate.parse("01/12/2022", formatter))
        );
        Geniteur geniteurAnoSaillieLicePortees = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, portees, true, true);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (personne), (g) -> (geniteurAnoSaillieLicePortees), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsAnyOf(MESSAGE_APPLICATION.GENITEUR_ALERTE_PORTEES.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_genealogie_complete() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurGenealogieIncomplete = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, false, true);
        Personne _personne = new Personne(1, "977", "GUADELOUPE", null);

        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (_personne), (g) -> (geniteurGenealogieIncomplete), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_GENEALOGIE.message);
    }

    @Test
    @DisplayName("Step9")
    void should_not_authorize_empreinte_adn() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurRequest = new Geniteur(1, SEXE.FEMELLE);
        Geniteur geniteurGenealogieEmpreinteAdn = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), null, TYPE_INSCRIPTION.DESCENDANCE, SEXE.FEMELLE, null, null, null, true, false);
        Personne _personne = new Personne(1, "977", "GUADELOUPE", null);

        ValidateGeniteur validateGeniteur = new GeniteurUseCase((p, r) -> (_personne), (g) -> (geniteurGenealogieEmpreinteAdn), (r) -> (race), (a) -> (commandeAdnEnCours));
        //When
        try {
            messages = validateGeniteur.execute(idEleveur, dateSaillie, geniteurRequest);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly(MESSAGE_APPLICATION.GENITEUR_EMPREINTE.message);
    }

}
