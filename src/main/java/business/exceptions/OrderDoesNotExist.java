package business.exceptions;

public class OrderDoesNotExist extends Exception{

    @Override
    public String getMessage() {
        return "order does not exist";
    }
}
