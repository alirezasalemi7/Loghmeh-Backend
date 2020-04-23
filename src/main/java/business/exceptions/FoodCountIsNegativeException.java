package business.exceptions;

public class FoodCountIsNegativeException extends Exception {

    String _message;

    public FoodCountIsNegativeException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
