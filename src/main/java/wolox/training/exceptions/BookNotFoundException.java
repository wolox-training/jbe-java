package wolox.training.exceptions;

/**
 * Thrown when a given book is not found
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(final String message) {
        super(message);
    }
}
