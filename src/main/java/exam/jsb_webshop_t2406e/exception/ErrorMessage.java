package exam.jsb_webshop_t2406e.exception;

import java.util.Date;
import java.util.Map;

public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
    // private Map<String, String> errors;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    // public void setErrors(Map<String, String> e) {
    // this.errors = e;
    // }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
