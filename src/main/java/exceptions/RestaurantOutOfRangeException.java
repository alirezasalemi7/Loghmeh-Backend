package exceptions;

public class RestaurantOutOfRangeException extends Exception{
    @Override
    public String getMessage() {
        return "restaurant out of range.";
    }
}
