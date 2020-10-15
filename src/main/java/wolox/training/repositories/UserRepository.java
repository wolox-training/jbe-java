package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.User;

/**
 * JPA repository to access to user data
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * find a book by its author
     *
     * @param username must not be null
     * @return the first found book by the given author or Optional#empty() if none found
     * @throws IllegalArgumentException if author is null
     */
    Optional<User> findOneByUsername(String username);
}
