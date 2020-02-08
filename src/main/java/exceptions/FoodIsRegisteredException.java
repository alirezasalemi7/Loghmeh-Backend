package exceptions;

public class FoodIsRegisteredException extends Exception {
    private String _message;

    public FoodIsRegisteredException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
