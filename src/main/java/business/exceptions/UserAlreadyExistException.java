package business.exceptions;

public class UserAlreadyExistException extends Exception {

    @Override
    public String getMessage() {
        return "user already exist.";
    }
}
