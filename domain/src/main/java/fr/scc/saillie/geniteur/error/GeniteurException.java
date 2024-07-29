package fr.scc.saillie.geniteur.error;

import java.util.List;

public class GeniteurException extends Exception {

    public GeniteurException(String message){
        this.message = message;
    }

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
