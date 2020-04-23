package business.exceptions;

public class FoodNotExistInCartException extends Exception{
    @Override
    public String getMessage() {
        return "food not exist in cart.";
    }
}
