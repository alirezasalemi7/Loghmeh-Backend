package exceptions;

public class OutOfRangeException extends Exception {
    private String _message;

    public OutOfRangeException(String message) {
        super(message);
        _message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
