package fr.scc.saillie.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.model.SEXE;
import fr.scc.saillie.validator.CheckDateFormat;
import fr.scc.saillie.validator.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Schema(description = "Geniteur request Information")
public class GeniteurRequest {

    @Schema(description = "geniteur Id", example = "123")
    Integer id;

    @NotNull(message = "L'éleveur doit être renseigné")
    @Schema(description = "eleveur id", example = "301")
    Integer idEleveur;    

    @NotNull(message = "La date de saillie doit être renseignée")
    @CheckDateFormat(pattern = "dd/MM/yyyy", message = "la date de saillie doit être renseignée au format jj/mm/aaaa")
    @Schema(description = "Date de saillie au format jj/mm/aaaa", example = "01/01/2023")
    String dateSaillie;    

    @NotNull(message = "Le sexe du géniteur doit être précisé")
    @ValueOfEnum(enumClass = SEXE.class, message = "le sexe du geniteur doit prendre pour valeur MALE ou FEMELLE")    
    @Schema(description = "Sexe annoncé du géniteur", example = "FEMELLE")
    String sexe;

    public static Geniteur convertToEntity(GeniteurRequest geniteurRequest) {
        return new Geniteur(geniteurRequest.getId()
            , SEXE.valueOf(geniteurRequest.getSexe()));
    }

    public static LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);        
        if ("".equals(date) || date == null) return null;
        return LocalDate.parse(date, formatter);
    }
}
