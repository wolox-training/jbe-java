package wolox.training.exceptions;

public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super("The book's id does not correspond to the data to update");
    }

    public BookIdMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookIdMismatchException(final String message) {
        super(message);
    }

    public BookIdMismatchException(final Throwable cause) {
        super(cause);
    }
}
