package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_BOOK;
import static wolox.training.util.MessageConstants.WRONG_SIZE;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
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

    private static Book book;
    public static final String PUBLISHER = "DEBATE";
    public static final String GENRE = "Natural History";
    public static final String YEAR = "2016";

    @BeforeAll
    static void setUpBook() {
        book = MockTestEntities.mockNewBook();
    }

    @Test
    void whenCreateBook_ThenIsPersisted() {
        bookRepository.save(book);
        Optional<Book> optionalBook = bookRepository.findById(1L);
        assertEquals(book, optionalBook.get(), WRONG_BOOK);
    }

    @Test
    void whenCreateBookWithSameIsbn_ThenThrowException() {
        bookRepository.save(book);
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
    void whenFindAllBooksByPublisherAndGenreAndYear_ThenReturnList() {
        bookRepository.saveAndFlush(book);

        List<Book> books = bookRepository.findAllByPublisherAndGenreAndYear(PUBLISHER, GENRE, YEAR);

        assertEquals(1, books.size(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.get(0).getPublisher());
        assertEquals(GENRE, books.get(0).getGenre());
        assertEquals(YEAR, books.get(0).getYear());
    }

    @Test
    void whenFindAllBooksByPublisherAndGenreAndYearWithNullPublisherAndGenre_ThenReturnList() {
        bookRepository.saveAndFlush(book);

        List<Book> books = bookRepository.findAllByPublisherAndGenreAndYear(null, null, YEAR);

        assertEquals(1, books.size(), WRONG_SIZE);
        assertEquals(YEAR, books.get(0).getYear());
    }

    @Test
    void whenFindAllBooksByPublisherAndGenreAndYearWithNullYearAndGenre_ThenReturnList() {
        bookRepository.saveAndFlush(book);

        List<Book> books = bookRepository.findAllByPublisherAndGenreAndYear(PUBLISHER, null, null);

        assertEquals(1, books.size(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.get(0).getPublisher());
    }

    @Test
    void whenFindAllBooksByPublisherAndGenreAndYearWithNulls_ThenReturnList() {
        bookRepository.saveAndFlush(book);

        List<Book> books = bookRepository.findAllByPublisherAndGenreAndYear(null, null, null);

        assertEquals(1, books.size(), WRONG_SIZE);
    }
}
