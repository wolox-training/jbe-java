package wolox.training.utils;

/**
 * This class contains constants that is using for the swagger documentation
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public final class SwaggerConstants {

    // Api operation
    public static final String USER_CONTROLLER_FIND_ALL = "find all users";
    public static final String USER_CONTROLLER_FIND_BY_ID = "find user by its id";
    public static final String USER_CONTROLLER_FIND_ONE_BY_USERNAME = "find user by its username";
    public static final String USER_CONTROLLER_CREATE = "Add a new user";
    public static final String USER_CONTROLLER_DELETE = "Remove an existing user";
    public static final String USER_CONTROLLER_UPDATE = "Update an existing user";
    public static final String USER_CONTROLLER_ADD_BOOK = "Add a new book to user's collection";
    public static final String USER_CONTROLLER_REMOVE_BOOK = "Remove a book from user's collection";
    // Api response
    public static final String RESPONSE_CODE_400 = "Malformed body request";
    public static final String RESPONSE_CODE_404 = "The resource is not found";
    public static final String RESPONSE_CODE_500 = "An unexpected error happened";
    public static final String USER_CONTROLLER_FIND_ALL_200 = "Succesfully retrieved users";
    public static final String USER_CONTROLLER_FIND_200 = "Succesfully retrieved user";
    public static final String USER_CONTROLLER_CREATE_201 = "User succesfully added";
    public static final String USER_CONTROLLER_UPDATE_204 = "User succesfully updated";
    public static final String USER_CONTROLLER_REMOVE_204 = "User succesfully removed";
    public static final String USER_CONTROLLER_ADD_BOOK_204 = "Book succesfully added";
    public static final String USER_CONTROLLER_REMOVE_BOOK_204 = "Book succesfully removed";

    private SwaggerConstants() {}
}



