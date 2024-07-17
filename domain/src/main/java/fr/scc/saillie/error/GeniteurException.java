package fr.scc.saillie.error;

import java.util.List;

public class GeniteurException extends Exception {

    public GeniteurException(String message, List<String> details){
        this.message = message;
        this.details = details;
    }

    //General error message about nature of error
    private String message;

    //Specific errors in API request processing
    private List<String> details;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }

}
