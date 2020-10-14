package wolox.training.utils;

public final class ErrorConstants {

    // Book error messages
    public static final String BOOK_NOT_FOUND = "Book id %s not found";
    public static final String BOOK_BY_AUTHOR_NOT_FOUND = "Books by author %s not found";
    public static final String BOOK_ID_MISMATCH = "The book id does not correspond with the body data";

    // User error messages
    public static final String BOOK_ALREADY_OWNED = "The user already has this book";
    public static final String USER_NOT_FOUND = "User id %s not found";
    public static final String USER_ID_MISMATCH = "The user id does not correspond with the body data";

    private ErrorConstants() {}
}
