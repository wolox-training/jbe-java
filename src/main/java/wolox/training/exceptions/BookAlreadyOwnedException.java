package wolox.training.exceptions;

/**
 * Thrown when a user already has the given book
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
