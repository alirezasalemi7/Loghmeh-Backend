package business.exceptions;

public class ServerInternalException extends Exception {

    @Override
    public String getMessage() {
        return "server internal error.";
    }
}
