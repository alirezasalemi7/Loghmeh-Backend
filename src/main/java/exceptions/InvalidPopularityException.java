package exceptions;

public class InvalidPopularityException extends Exception {

    private String _message;

    public InvalidPopularityException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
