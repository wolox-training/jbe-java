package wolox.training.utils;

public final class ErrorConstants {

    // Book error messages
    public static final String BOOK_NOT_FOUND = "Book id %s not found";
    public static final String BOOK_BY_AUTHOR_NOT_FOUND = "Books by author %s not found";
    public static final String BOOK_ID_MISMATCH = "The book id does not correspond with the body data";

    // Book preconditions error messages
    public static final String OBLIGATORY_AUTHOR_FIELD = "The author field is required";
    public static final String OBLIGATORY_IMAGE_FIELD = "The image field is required";
    public static final String OBLIGATORY_ISBN_FIELD = "The isbn field is required";
    public static final String OBLIGATORY_PAGES_FIELD = "The pages field is required";
    public static final String OBLIGATORY_PUBLISHER_FIELD = "The publisher field is required";
    public static final String OBLIGATORY_TITLE_FIELD = "The title field is required";
    public static final String OBLIGATORY_SUBTITLE_FIELD = "The subtitle field is required";
    public static final String OBLIGATORY_YEAR_FIELD = "The year field is required";

    public static final String PAGES_LESS_THAN_ZERO = "The pages must be greater than zero";
    public static final String INVALID_ISBN = "The isbn must be a number";
    public static final String INVALID_PAGES = "The pages must be a number";
    public static final String INVALID_YEAR = "The year must be a number";

    // User error messages
    public static final String BOOK_ALREADY_OWNED = "The user already has this book";
    public static final String USER_NOT_FOUND = "User id %s not found";
    public static final String USER_ID_MISMATCH = "The user id does not correspond with the body data";

    // User preconditions error messages
    public static final String OBLIGATORY_NAME_FIELD = "The name field is required";
    public static final String OBLIGATORY_USERNAME_FIELD = "The username field is required";
    public static final String OBLIGATORY_BIRTHDATE_FIELD = "The birthdate field is required";
    public static final String INVALID_BIRTHDATE = "The birthdate must be earlier to the current date";
    public static final String BOOK_NOT_NULL = "The book data can't be null";

    private ErrorConstants() {}
}
