package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
