package wolox.training.controllers;

import static wolox.training.utils.ErrorConstants.BOOK_NOT_FOUND;
import static wolox.training.utils.ErrorConstants.USER_ID_MISMATCH;
import static wolox.training.utils.ErrorConstants.USER_NOT_FOUND;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND,
         id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid User user) {
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.delete(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody @Valid User user) {
        if (!user.getId().equals(id)) {
            throw new UserIdMismatchException(USER_ID_MISMATCH);
        } else if (userRepository.existsById(id)) {
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

    @PostMapping("/{id}/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBook(@PathVariable Long id, @RequestBody @Valid Book book) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (bookRepository.existsById(book.getId())) {
            user.addBook(book);
            userRepository.save(user);
        } else {
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND, book.getId()));
        }
    }

    @DeleteMapping("/{id}/books/{book_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBook(@PathVariable Long id, @RequestBody @Valid Book book) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (bookRepository.existsById(book.getId())) {
            user.removeBook(book);
            userRepository.save(user);
        } else {
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND, book.getId()));
        }
    }
}
