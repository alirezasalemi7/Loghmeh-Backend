package business.exceptions;

public class NegativeChargeAmountException extends Exception {
    private String _message;

    public NegativeChargeAmountException(String message) {
        super(message);
        _message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
