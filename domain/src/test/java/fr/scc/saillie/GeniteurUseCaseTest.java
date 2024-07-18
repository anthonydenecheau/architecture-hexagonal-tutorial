package fr.scc.saillie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.scc.saillie.geniteur.GeniteurUseCase;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.Race;

public class GeniteurUseCaseTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    Geniteur geniteur = null;
    Race race;

    @BeforeEach
    public void setUp() {
        this.geniteur = new Geniteur(1, LocalDate.parse("01/01/2022", formatter));
        this.race = new Race(56,12);
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
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((a) -> (race));
        //When
        String result = null;
        try  {
            result = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
            result = e.getMessage();
        }
        //Then
        assertThat(result).isEqualTo("Le géniteur est validé");
    }

    @Test
    @DisplayName("Step3")
    void should_not_authorize_geniteur() {
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/01/2021", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((a) -> (race));
        //When
        String result = null;
        try {
            result = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
            result = e.getMessage();
        }

        //Then
        assertThat(result).isEqualTo("le géniteur est née avant la saillie");
    }

    @Test
    @DisplayName("Step5")
    void should_not_authorize_age_minimum() {
        //Given
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2022", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((a) -> (race));
        //When
        String result = null;
        try {
            result = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
            result = e.getMessage();
        }

        //Then
        assertThat(result).isEqualTo("le géniteur n'est pas en âge de reproduire");
    }

    @Test
    @DisplayName("Step5")
    void should_authorize_age_minimum() {
        //Given
        //Given
        LocalDate dateSaillie = LocalDate.parse("01/08/2024", formatter);
        ValidateGeniteur validateGeniteur = new GeniteurUseCase((a) -> (race));
        //When
        String result = null;
        try {
            result = validateGeniteur.execute(dateSaillie, this.geniteur);
        } catch (Exception e) {
            result = e.getMessage();
        }

        //Then
        assertThat(result).isEqualTo("Le géniteur est validé");
    }


}
