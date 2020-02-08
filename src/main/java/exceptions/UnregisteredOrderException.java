package exceptions;

public class UnregisteredOrderException extends Exception {

    private String _message;

    public UnregisteredOrderException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
