package fr.scc.saillie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.scc.saillie.geniteur.GeniteurUseCase;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.model.Race;
import fr.scc.saillie.geniteur.model.SEXE;

public class GeniteurUseCaseTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    Geniteur geniteur = null;
    Race race;
    List<Message> messages;

    @BeforeEach
    public void setUp() {
        this.geniteur = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), SEXE.FEMELLE);
        this.race = new Race(56,12);
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
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((g) -> (geniteur), (r) -> (race));
        //When
        try  {
            messages = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly("le géniteur est validé");
    }

    @Test
    @DisplayName("Step3")
    void should_not_authorize_geniteur() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/01/2021", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((g) -> (geniteur), (r) -> (race));
        //When
        try {
            messages = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly("le géniteur est née après la saillie");
    }

    @Test
    @DisplayName("Step5")
    void should_not_authorize_age_minimum() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2022", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((g) -> (geniteur), (r) -> (race));
        //When
        try {
            messages = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly("le géniteur n'est pas en âge de reproduire");
    }

    @Test
    @DisplayName("Step5")
    void should_authorize_age_minimum() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((g) -> (geniteur), (r) -> (race));
        //When
        try {
            messages = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly("le géniteur est validé");
    }

    @Test
    @DisplayName("Step8")
    void should_not_authorize_sexe() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        Geniteur geniteurAnoSexe = new Geniteur(1, 56, LocalDate.parse("01/01/2022", formatter), SEXE.MALE);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((g) -> (geniteur), (r) -> (race));
        //When
        try {
            messages = validateGeniteur.execute(dateSaillie, geniteurAnoSexe);
        } catch (Exception e) {
        }
        //Then
        assertThat(messages).extracting(m->m.message()).containsExactly("le géniteur n'est pas du bon sexe");
    }

}
