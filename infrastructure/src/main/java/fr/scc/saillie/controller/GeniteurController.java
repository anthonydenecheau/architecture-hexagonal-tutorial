package fr.scc.saillie.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.scc.saillie.dto.GeniteurRequest;
import fr.scc.saillie.geniteur.api.ValidateGeniteur;
import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.LEVEL;
import fr.scc.saillie.geniteur.model.Message;
import fr.scc.saillie.geniteur.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Geniteur", description = "Geniteur  Api")
@RestController
public class GeniteurController {

    private final ValidateGeniteur validateGeniteur;

    public GeniteurController(ValidateGeniteur validateGeniteur) {
        this.validateGeniteur = validateGeniteur;
    }

    @PostMapping(path = "/validateGeniteur", produces = "application/json; charset=UTF-8", consumes = "application/json")
    @Operation(
      summary = "Validation d'un géniteur",
      description = "Validation d'un géniteur lors d'une saillie.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "successful operation", content = { @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", description = "something's wrong", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", description = "something's crashed", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> validate(@Valid @RequestBody GeniteurRequest geniteurRequest, BindingResult result) {
        try {
            List<Message> messages = new ArrayList<Message>();
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    messages.add(new Message(LEVEL.ERROR, "990", error.getDefaultMessage()));
                }
                return new ResponseEntity<List<Message>>(messages, HttpStatus.BAD_REQUEST); 
            }
            
            messages = validateGeniteur.execute(
                geniteurRequest.getIdEleveur()
                , DateUtils.convertStringToLocalDate(geniteurRequest.getDateSaillie())
                , GeniteurRequest.convertToEntity(geniteurRequest))
            ;
            
            if (!messages.stream().anyMatch(message -> LEVEL.ERROR.equals(message.level())))
                return new ResponseEntity<List<Message>>(messages, HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<List<Message>>(messages, HttpStatus.BAD_REQUEST);

        } catch (GeniteurException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}

