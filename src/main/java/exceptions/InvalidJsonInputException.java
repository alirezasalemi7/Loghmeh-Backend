package exceptions;

public class InvalidJsonInputException extends Exception {
    @Override
    public String getMessage() {
        return "invalid json input format.";
    }
}
