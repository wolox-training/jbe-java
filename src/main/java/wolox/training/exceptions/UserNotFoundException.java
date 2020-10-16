package wolox.training.exceptions;

/**
 * Thrown when a given user is not found
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
