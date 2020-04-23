package business.exceptions;

public class RestaurantIsRegisteredException extends Exception {

    private String _message;

    public RestaurantIsRegisteredException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
