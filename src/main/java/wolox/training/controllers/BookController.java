package wolox.training.controllers;

import static wolox.training.utils.ErrorConstants.BOOK_ID_MISMATCH;
import static wolox.training.utils.ErrorConstants.BOOK_ID_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.BookDTO;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

/**
 * Web controller to handle the books resource request
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookRepository bookRepository;
    private final OpenLibraryService openLibraryService;

    @Autowired
    public BookController(BookRepository bookRepository, OpenLibraryService openLibraryService) {
        this.bookRepository = bookRepository;
        this.openLibraryService = openLibraryService;
    }

    @GetMapping
    public List<Book> findAll(
        @RequestParam(required = false, defaultValue = "") String author,
        @RequestParam(required = false, defaultValue = "") String genre,
        @RequestParam(required = false, defaultValue = "") String image,
        @RequestParam(required = false, defaultValue = "") String pages,
        @RequestParam(required = false, defaultValue = "") String publisher,
        @RequestParam(required = false, defaultValue = "") String subtitle,
        @RequestParam(required = false, defaultValue = "") String title,
        @RequestParam(required = false, defaultValue = "") String year
    ) {
        return bookRepository.findAll(author, genre, image, pages, publisher, subtitle, title, year);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
            new BookNotFoundException(String.format(BOOK_ID_NOT_FOUND, id))
        );
    }

    @GetMapping(params = "isbn")
    public ResponseEntity<Book> findByIsbn(@RequestParam String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            return new ResponseEntity<>(optionalBook.get(), HttpStatus.OK);
        } else {
            BookDTO bookDTO = openLibraryService.bookInfo(isbn);
            Book book = bookRepository.save(bookDTO.toBook());

            return new ResponseEntity<>(book, HttpStatus.CREATED);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Book book) {
        bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book b = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(String.format(BOOK_ID_NOT_FOUND, id)));
        bookRepository.delete(b);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody @Valid Book book) {
        if (!book.getId().equals(id)) {
            throw new BookIdMismatchException(BOOK_ID_MISMATCH);
        } else {
            bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format(BOOK_ID_NOT_FOUND, id)));
            return bookRepository.save(book);
        }
    }
}
