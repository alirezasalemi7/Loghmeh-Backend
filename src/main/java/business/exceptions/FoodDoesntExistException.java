package business.exceptions;

public class FoodDoesntExistException extends Exception {
    String _message;
    public FoodDoesntExistException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}

