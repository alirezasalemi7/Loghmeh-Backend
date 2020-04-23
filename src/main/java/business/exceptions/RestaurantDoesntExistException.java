package business.exceptions;


public class RestaurantDoesntExistException extends Exception {

    private String _message;

    public RestaurantDoesntExistException(String message) {
        super(message);
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
