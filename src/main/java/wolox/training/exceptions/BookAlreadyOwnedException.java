package wolox.training.exceptions;

public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
