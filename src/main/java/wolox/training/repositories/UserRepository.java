package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * @param startDate can be null
     * @param endDate   can be null
     * @param name      can be null
     * @return all found users
     */
    @Query("SELECT u FROM User u WHERE (:startDate IS NULL OR :endDate IS NULL OR u.birthdate BETWEEN :startDate AND "
        + ":endDate) OR (:name IS NULL OR UPPER(u.name) LIKE UPPER(:name))")
    List<User> findAllByBirthdateBetweenAndNameContainingIgnoreCase(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate, @Param("name") String name);
}
