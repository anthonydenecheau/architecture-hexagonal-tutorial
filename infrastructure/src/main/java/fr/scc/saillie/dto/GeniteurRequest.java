package fr.scc.saillie.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.validator.CheckDateFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class GeniteurRequest {

    Integer id;

    @NotNull(message = "La date de saillie doit être renseignée")
    @CheckDateFormat(pattern = "dd/MM/yyyy", message = "la date de saillie doit être renseignée au format jj/mm/aaaa")
    String dateSaillie;    

    @NotNull(message = "La date de naissance doit être renseignée")
    @CheckDateFormat(pattern = "dd/MM/yyyy", message = "la date de naissance doit être renseignée au format jj/mm/aaaa")
    String dateNaissance;    

    public static Geniteur convertToEntity(GeniteurRequest geniteurRequest) {
        return new Geniteur(geniteurRequest.getId()
            , convertStringToDate(geniteurRequest.getDateNaissance()));
    }

    public static LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);        
        if ("".equals(date) || date == null) return null;
        return LocalDate.parse(date, formatter);
    }
}
