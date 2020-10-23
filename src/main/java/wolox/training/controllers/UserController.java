package wolox.training.controllers;

import static wolox.training.utils.ErrorConstants.BOOK_ID_NOT_FOUND;
import static wolox.training.utils.ErrorConstants.USER_BY_USERNAME_NOT_FOUND;
import static wolox.training.utils.ErrorConstants.USER_ID_MISMATCH;
import static wolox.training.utils.ErrorConstants.USER_NOT_FOUND;
import static wolox.training.utils.SwaggerConstants.RESPONSE_CODE_400;
import static wolox.training.utils.SwaggerConstants.RESPONSE_CODE_401;
import static wolox.training.utils.SwaggerConstants.RESPONSE_CODE_404;
import static wolox.training.utils.SwaggerConstants.RESPONSE_CODE_500;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_ADD_BOOK;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_ADD_BOOK_204;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_AUTHENTICATED_USER;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_CREATE;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_CREATE_201;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_DELETE;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_FIND_ALL;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_FIND_ALL_200;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_FIND_200;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_FIND_BY_ID;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_FIND_ONE_BY_USERNAME;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_REMOVE_204;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_REMOVE_BOOK;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_REMOVE_BOOK_204;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_UPDATE;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_UPDATE_200;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_UPDATE_PASSWORD;
import static wolox.training.utils.SwaggerConstants.USER_CONTROLLER_UPDATE_PASSWORD_204;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

/**
 * Web controller to handle the users resource request
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @ApiOperation(value = USER_CONTROLLER_FIND_ALL)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = USER_CONTROLLER_FIND_ALL_200, responseContainer = "List",
            response = User.class),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401)
    })
    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @ApiOperation(value = USER_CONTROLLER_FIND_BY_ID, response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = USER_CONTROLLER_FIND_200),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404)
    })
    @GetMapping("/{id}")
    public User findById(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND,
            id)));
    }

    @ApiOperation(value = USER_CONTROLLER_FIND_ONE_BY_USERNAME)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = USER_CONTROLLER_FIND_200, response = User.class),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404)
    })
    @GetMapping(params = "username")
    public User findOneByUsername(@ApiParam(name = "username", required = true) @RequestParam String username) {
        return userRepository.findOneByUsername(username).orElseThrow(()
            -> new UserNotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, username)));
    }

    @ApiOperation(value = USER_CONTROLLER_AUTHENTICATED_USER)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = USER_CONTROLLER_FIND_200),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @GetMapping("/currentUser")
    public ResponseEntity<Object> getAuthenticatedUser(Authentication authentication) {
        String jsonResponse = "{\"user\": \"%s\"}";
        return new ResponseEntity<>(String.format(jsonResponse, authentication.getName()), HttpStatus.OK);
    }

    @ApiOperation(value = USER_CONTROLLER_CREATE)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = USER_CONTROLLER_CREATE_201),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid User user) {
        userRepository.save(user);
    }

    @ApiOperation(value = USER_CONTROLLER_DELETE)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = USER_CONTROLLER_REMOVE_204),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiParam(value = "User's id", required = true) @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.delete(user);
    }

    @ApiOperation(value = USER_CONTROLLER_UPDATE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = USER_CONTROLLER_UPDATE_200, response = User.class),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @PutMapping("/{id}")
    public User update(
        @ApiParam(value = "User's id", required = true) @PathVariable Long id, @RequestBody @Valid User user) {

        if (!user.getId().equals(id)) {
            throw new UserIdMismatchException(USER_ID_MISMATCH);
        } else if (userRepository.existsById(id)) {
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

    @ApiOperation(value = USER_CONTROLLER_ADD_BOOK)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = USER_CONTROLLER_ADD_BOOK_204),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @PatchMapping("/{id}/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBook(
        @ApiParam(value = "User's id", required = true) @PathVariable Long id, @RequestBody @Valid Book book) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (bookRepository.existsById(book.getId())) {
            user.addBook(book);
            userRepository.save(user);
        } else {
            throw new BookNotFoundException(String.format(BOOK_ID_NOT_FOUND, book.getId()));
        }
    }

    @ApiOperation(value = USER_CONTROLLER_REMOVE_BOOK)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = USER_CONTROLLER_REMOVE_BOOK_204),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @PatchMapping("/{id}/books/{book_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBook(
        @ApiParam(value = "User's id", required = true) @PathVariable Long id, @RequestBody @Valid Book book) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (bookRepository.existsById(book.getId())) {
            user.removeBook(book);
            userRepository.save(user);
        } else {
            throw new BookNotFoundException(String.format(BOOK_ID_NOT_FOUND, book.getId()));
        }
    }

    @ApiOperation(value = USER_CONTROLLER_UPDATE_PASSWORD)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = USER_CONTROLLER_UPDATE_PASSWORD_204),
        @ApiResponse(code = 400, message = RESPONSE_CODE_400),
        @ApiResponse(code = 401, message = RESPONSE_CODE_401),
        @ApiResponse(code = 404, message = RESPONSE_CODE_404),
        @ApiResponse(code = 500, message = RESPONSE_CODE_500)
    })
    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
        @ApiParam(value = "User's id", required = true) @PathVariable Long id, @RequestBody @Valid User user) {

        if (!user.getId().equals(id)) {
            throw new UserIdMismatchException(USER_ID_MISMATCH);
        } else if (userRepository.existsById(id)) {
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }
}
