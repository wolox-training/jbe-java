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
     * find all books by the given params
     *
     * @param author must not be null
     * @param genre must not be null
     * @param image must not be null
     * @param pages must not be null
     * @param publisher must not be null
     * @param subtitle must not be null
     * @param title must not be null
     * @param year must not be null
     * @return all books found
     * @throws IllegalArgumentException if some argument is null
     */
    @Query("SELECT b FROM Book b WHERE (LENGTH(:author) = 0 OR UPPER(b.author) LIKE CONCAT('%', UPPER(:author), '%')) "
        + "AND (LENGTH(:genre) = 0 OR UPPER(b.genre) LIKE CONCAT('%', UPPER(:genre), '%')) AND (LENGTH(:image) = 0 "
        + "OR UPPER(b.image) LIKE CONCAT('%',UPPER(:image),'%')) AND (LENGTH(:pages) = 0 OR UPPER(b.pages) LIKE "
        + "CONCAT('%',UPPER(:pages),'%')) AND (LENGTH(:publisher) = 0 OR UPPER(b.publisher) LIKE "
        + "CONCAT('%',UPPER(:publisher),'%')) AND (LENGTH(:subtitle) = 0 OR UPPER(b.subtitle) LIKE "
        + "CONCAT('%',UPPER(:subtitle),'%')) AND (LENGTH(:title) = 0 OR UPPER(b.title) LIKE "
        + "CONCAT('%',UPPER(:title),'%')) AND (LENGTH(:year) = 0 OR UPPER(b.year) LIKE "
        + "CONCAT('%',UPPER(:year),'%'))")
    List<Book> findAll(@Param("author") String author, @Param("genre") String genre, @Param("image") String image,
        @Param("pages") String pages, @Param("publisher") String publisher, @Param("subtitle") String subtitle,
        @Param("title") String title, @Param("year") String year);

    /**
     * find a book by its ISBN
     *
     * @param isbn must not be null
     * @return the found book by the given ISBN or Optional#empty() if none found
     * @throws IllegalArgumentException if isbn is null
     */
    Optional<Book> findByIsbn(String isbn);
}
