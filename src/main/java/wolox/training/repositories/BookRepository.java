package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

/**
 * JPA repository to access to book data
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * find a book by its author
     *
     * @param author must not be null
     * @return the first found book by the given author or Optional#empty() if none found
     * @throws IllegalArgumentException if author is null
     */
    Optional<Book> findOneByAuthor(String author);

    /**
     * find all books by the same publisher, genre and year
     *
     * @param publisher can be null
     * @param genre     can be null
     * @param year      can be null
     * @return All books found by the matches
     */
    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher = :publisher) OR (:genre IS NULL OR b.genre"
        + " = :genre) OR (:year IS NULL OR b.year = :year)")
    List<Book> findAllByPublisherAndGenreAndYear(@Param("publisher") String publisher, @Param("genre") String genre,
        @Param("year") String year);

    Optional<Book> findByIsbn(String isbn);
}
