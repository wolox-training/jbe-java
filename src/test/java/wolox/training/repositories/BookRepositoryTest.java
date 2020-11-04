package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_BOOK;
import static wolox.training.util.MessageConstants.WRONG_PAGE;
import static wolox.training.util.MessageConstants.WRONG_PAGE_SIZE;
import static wolox.training.util.MessageConstants.WRONG_SIZE;
import static wolox.training.util.ParamsConstants.AUTHOR;
import static wolox.training.util.ParamsConstants.DEFAULT_PAGEABLE;
import static wolox.training.util.ParamsConstants.GENRE;
import static wolox.training.util.ParamsConstants.IMAGE;
import static wolox.training.util.ParamsConstants.PAGES;
import static wolox.training.util.ParamsConstants.PUBLISHER;
import static wolox.training.util.ParamsConstants.SUBTITLE;
import static wolox.training.util.ParamsConstants.TITLE;
import static wolox.training.util.ParamsConstants.YEAR;

import java.io.IOException;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Page<Book> books = bookRepository.findAll("", "", "", "", "", "", "", "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(27, books.getTotalElements(), WRONG_SIZE);
    }

    @Test
    void whenFindAllBooksByPublisherAndGenreAndYear_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", GENRE, "", "", PUBLISHER, "", "", YEAR, DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(1, books.getTotalElements(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.getContent().get(0).getPublisher());
        assertEquals(GENRE, books.getContent().get(0).getGenre());
        assertEquals(YEAR, books.getContent().get(0).getYear());
    }

    @Test
    void whenFindAllBooksByYear_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", "", "", "", "", "", "", YEAR, DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(6, books.getTotalElements(), WRONG_SIZE);
        assertEquals(YEAR, books.getContent().get(0).getYear());
    }

    @Test
    void whenFindAllBooksByPublisher_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", "", "", "", PUBLISHER, "", "", "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(18, books.getTotalElements(), WRONG_SIZE);
        assertEquals(PUBLISHER, books.getContent().get(0).getPublisher());
    }

    @Test
    void whenFindAllBooksByImage_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", "", IMAGE, "", "", "", "", "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(27, books.getTotalElements(), WRONG_SIZE);
        assertEquals(IMAGE, books.getContent().get(19).getImage());
    }

    @Test
    void whenFindAllBooksByTitle_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", "", "", "", "", "", TITLE, "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(2, books.getTotalElements(), WRONG_SIZE);
        assertEquals(TITLE, books.getContent().get(0).getTitle());
    }

    @Test
    void whenFindAllBooksBySubtitle_ThenReturnList() {
        Page<Book> books = bookRepository.findAll("", "", "", "", "", SUBTITLE, "", "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(2, books.getTotalElements(), WRONG_SIZE);
        assertTrue(books.getContent().get(0)
            .getSubtitle()
            .toUpperCase()
            .contains(SUBTITLE.toUpperCase()));
    }

    @Test
    void whenFindAllBooksByAuthor_ThenReturnList() {
        Page<Book> books = bookRepository.findAll(AUTHOR, "", "", "", "", "", "", "", DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(6, books.getTotalElements(), WRONG_SIZE);
        assertEquals(AUTHOR.toUpperCase(), books.getContent().get(2).getAuthor().toUpperCase());
    }

    @Test
    void whenFindAllBooksByAllFilters_ThenReturnList() {
        Page<Book> books = bookRepository.findAll(AUTHOR, GENRE, IMAGE, PAGES, PUBLISHER, SUBTITLE, TITLE, YEAR,
            DEFAULT_PAGEABLE);

        assertNotNull(books);
        assertEquals(0, books.getTotalElements(), WRONG_SIZE);
    }

    @Test
    void whenFindAllBooksWithPageSize_ThenReturnPage() {
        Page<Book> bookPage = bookRepository.findAll("", "", "", "", "", "", "", "", PageRequest.of(0, 5));

        assertNotNull(bookPage);
        assertEquals(0, bookPage.getNumber(), WRONG_PAGE);
        assertEquals(5, bookPage.getSize(), WRONG_PAGE_SIZE);
        assertEquals(5, bookPage.getNumberOfElements(), WRONG_SIZE);
    }

    @Test
    void whenFindAllBooksWithPageSizeAndSortedByTitle_ThenReturnSortedPage() {
        Page<Book> bookPage = bookRepository.findAll("", "", "", "", "", "", "", "",
            PageRequest.of(0, 5, Sort.by("title").ascending()));

        assertNotNull(bookPage);
        assertEquals(0, bookPage.getNumber(), WRONG_PAGE);
        assertEquals(5, bookPage.getSize(), WRONG_PAGE_SIZE);
        assertEquals(5, bookPage.getNumberOfElements(), WRONG_SIZE);
        assertTrue(bookPage.getSort().isSorted());
        assertEquals("9781603090476", bookPage.getContent().get(0).getIsbn());
    }
}
