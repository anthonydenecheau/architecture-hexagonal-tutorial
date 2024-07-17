package fr.scc.saillie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.scc.saillie. api.ValidateGeniteur;
import fr.scc.saillie.model.Geniteur;

public class GeniteurUseCaseTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    Geniteur geniteur = null;

    @BeforeEach
    public void setUp() {
        this.geniteur = new Geniteur(1, LocalDate.parse("01/01/2022", formatter));
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
        ValidateGeniteur validateGeniteur = new GeniteurUseCase();
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
        ValidateGeniteur validateGeniteur = new GeniteurUseCase();
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

}
