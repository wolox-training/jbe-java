package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import wolox.training.exceptions.BookAlreadyOwnedException;
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

        String publisher = "DEBATE";
        String year = "2016";

        List<Book> books = bookRepository.findAllByPublisherAndGenreAndYear(publisher, null, year);

        assertEquals(1, books.size(), WRONG_SIZE);
        assertEquals(publisher, books.get(0).getPublisher());
        assertEquals(year, books.get(0).getYear());
        assertNull(books.get(0).getGenre());
    }
}
