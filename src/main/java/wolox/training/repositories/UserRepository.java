package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
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
     * @return the first found user by the given username or Optional#empty() if none found
     * @throws IllegalArgumentException if author is null
     */
    Optional<User> findOneByUsername(String username);

    /**
     * find all user between two birthdates and containing a character sequence in their name
     *
     * @param startDate must not be null
     * @param endDate must not be null
     * @param name must not be null
     * @return all found users
     * @throws IllegalArgumentException if some argument is null
     */
    List<User> findAllByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate startDate, LocalDate endDate,
        String name);
}
