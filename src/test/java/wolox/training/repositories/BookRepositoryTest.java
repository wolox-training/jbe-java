package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_BOOK;
import static wolox.training.util.MessageConstants.WRONG_SIZE;
import static wolox.training.util.ParamsConstants.AUTHOR;
import static wolox.training.util.ParamsConstants.GENRE;
import static wolox.training.util.ParamsConstants.IMAGE;
import static wolox.training.util.ParamsConstants.PAGES;
import static wolox.training.util.ParamsConstants.PUBLISHER;
import static wolox.training.util.ParamsConstants.SUBTITLE;
import static wolox.training.util.ParamsConstants.TITLE;
import static wolox.training.util.ParamsConstants.YEAR;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.models.Book;
import wolox.training.util.MockTestEntities;

/**
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book book;

    @BeforeEach
    void setUpBook() throws IOException {
        book = MockTestEntities.mockNewBook();
        bookRepository.saveAll(MockTestEntities.mockBooks());
        bookRepository.flush();
    }

    @Test
    void whenCreateBook_ThenIsPersisted() {
        long id = bookRepository.save(book).getId();
        Optional<Book> optionalBook = bookRepository.findById(id);
        assertEquals(book, optionalBook.get(), WRONG_BOOK);
    }

    @Test
    void whenCreateBookWithSameIsbn_ThenThrowException() {
        bookRepository.save(book);
        book = MockTestEntities.mockNewBook();

        assertThrows(DataIntegrityViolationException.class, () -> bookRepository.saveAndFlush(book), EXCEPTION_THROWN);
    }

    @Test
    void whenCreateUserWithoutAuthor_ThenThrowException() {
        Book b = new Book();
        b.setTitle("Homo Deus");
        b.setSubtitle("A Brief History of tomorrow");
        b.setImage("homo_deus.png");
        b.setIsbn("9788499926711");
        b.setPages("496");
        b.setYear("2016");
        b.setPublisher("DEBATE");

        assertThrows(ConstraintViolationException.class, () -> bookRepository.saveAndFlush(b), EXCEPTION_THROWN);
    }

    @Test
    void whenFindAllBooksWithEmptyFilters_ThenReturnAllBooks() {
        List<Book> books = bookRepository.findAll("", "", "", "", "", "", "", "");

        assertNotNull(books);
        assertEquals(28, books.size(), WRONG_SIZE);
    }

    @Test
    void whenFindAllBooksByPublisherAndGenreAndYear_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", GENRE, "", "", PUBLISHER, "", "", YEAR);

        assertNotNull(books);
        assertEquals(1, books.size(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.get(0).getPublisher());
        assertEquals(GENRE, books.get(0).getGenre());
        assertEquals(YEAR, books.get(0).getYear());
    }

    @Test
    void whenFindAllBooksByYear_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", "", "", "", "", "", "", YEAR);

        assertNotNull(books);
        assertEquals(6, books.size(), WRONG_SIZE);
        assertEquals(YEAR, books.get(0).getYear());
    }

    @Test
    void whenFindAllBooksByPublisher_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", "", "", "", PUBLISHER, "", "", "");

        assertNotNull(books);
        assertEquals(19, books.size(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.get(0).getPublisher());
    }

    @Test
    void whenFindAllBooksByImage_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", "", IMAGE, "", "", "", "", "");

        assertNotNull(books);
        assertEquals(28, books.size(), WRONG_SIZE);
        assertEquals(IMAGE, books.get(27).getImage());
    }

    @Test
    void whenFindAllBooksByTitle_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", "", "", "", "", "", TITLE, "");

        assertNotNull(books);
        assertEquals(2, books.size(), WRONG_SIZE);
        assertEquals(TITLE, books.get(0).getTitle());
    }

    @Test
    void whenFindAllBooksBySubtitle_ThenReturnList() {
        List<Book> books = bookRepository.findAll("", "", "", "", "", SUBTITLE, "", "");

        assertNotNull(books);
        assertEquals(2, books.size(), WRONG_SIZE);
        assertTrue(books.get(0)
            .getSubtitle()
            .toUpperCase()
            .contains(SUBTITLE.toUpperCase()));
    }

    @Test
    void whenFindAllBooksByAuthor_ThenReturnList() {
        List<Book> books = bookRepository.findAll(AUTHOR, "", "", "", "", "", "", "");

        assertNotNull(books);
        assertEquals(6, books.size(), WRONG_SIZE);
        assertEquals(AUTHOR.toUpperCase(), books.get(2).getAuthor().toUpperCase());
    }

    @Test
    void whenFindAllBooksByAllFilters_ThenReturnList() {
        List<Book> books = bookRepository.findAll(AUTHOR, GENRE, IMAGE, PAGES, PUBLISHER, SUBTITLE, TITLE, YEAR);

        assertNotNull(books);
        assertEquals(0, books.size(), WRONG_SIZE);
    }
}
