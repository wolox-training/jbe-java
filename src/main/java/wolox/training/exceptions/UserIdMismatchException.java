package wolox.training.exceptions;

/**
 * Thrown when a given id mismatch with the received id in the body request
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException(String message) {
        super(message);
    }
}
