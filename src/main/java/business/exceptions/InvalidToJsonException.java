package business.exceptions;

public class InvalidToJsonException extends Exception {

    @Override
    public String getMessage() {
        return "invalid object. cannot convert to json.";
    }
}
