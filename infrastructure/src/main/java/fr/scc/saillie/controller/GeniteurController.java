package fr.scc.saillie.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.scc.saillie.api.ValidateGeniteur;
import fr.scc.saillie.dto.GeniteurRequest;
import fr.scc.saillie.error.GeniteurException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class GeniteurController {

    private final ValidateGeniteur validateGeniteur;

    public GeniteurController(ValidateGeniteur validateGeniteur) {
        this.validateGeniteur = validateGeniteur;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/validateGeniteur")
    public ResponseEntity<?> validate(@Valid @RequestBody GeniteurRequest geniteurRequest, BindingResult result) {
        List<String> details = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    details.add(error.getDefaultMessage());
                }
                GeniteurException _error = new GeniteurException("Validation failed", details);
                return new ResponseEntity(_error, HttpStatus.BAD_REQUEST); 
            }
            
            var validation = validateGeniteur.execute(
                GeniteurRequest.convertStringToDate(geniteurRequest.getDateSaillie())
                , GeniteurRequest.convertToEntity(geniteurRequest))
            ;
            
            return new ResponseEntity<String>(validation, HttpStatus.ACCEPTED);
        } catch (GeniteurException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}

