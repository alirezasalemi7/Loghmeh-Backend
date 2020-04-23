package business.exceptions;

public class CreditIsNotEnoughException extends Exception {
    String _message;
    public CreditIsNotEnoughException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
