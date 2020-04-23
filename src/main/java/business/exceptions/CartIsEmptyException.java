package business.exceptions;

public class CartIsEmptyException extends Exception {
    String _message;
    public CartIsEmptyException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
