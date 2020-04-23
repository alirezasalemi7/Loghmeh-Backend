package business.exceptions;

public class InvalidPriceException extends Exception {

    private String _message;

    public InvalidPriceException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
