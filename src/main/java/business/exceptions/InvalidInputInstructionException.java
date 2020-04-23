package business.exceptions;

public class InvalidInputInstructionException extends Exception {

    @Override
    public String getMessage() {
        return "invalid instruction.";
    }
}
