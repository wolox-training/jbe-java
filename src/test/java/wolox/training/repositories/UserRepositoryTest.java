package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_SIZE;
import static wolox.training.util.MessageConstants.WRONG_USER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wolox.training.models.User;
import wolox.training.util.MockTestEntities;

/**
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenCreateUser_ThenIsPersisted() {
        User user = MockTestEntities.mockNewUser();

        userRepository.save(user);
        Optional<User> optionalUser = userRepository.findById(1L);

        assertEquals(optionalUser.get(), user, WRONG_USER);
    }

    @Test
    void whenCreateUserWithoutRequiredField_ThenThrowException() {
        User user = new User();
        user.setName("Tes User");
        user.setUsername("tu");

        assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user), EXCEPTION_THROWN);
    }

    @Test
    void whenCreateEmptyUser_ThenThrowException() {
        User u = new User();

        assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(u), EXCEPTION_THROWN);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdatesAndContainingCharactersInName_ThenReturnList() {
        LocalDate startDate = LocalDate.of(1940, 12, 13);
        LocalDate endDate = LocalDate.of(2000, 4, 5);
        String name = "ua";

        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);
        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(startDate, endDate,
            name);
        assertEquals(1, users.size(), WRONG_SIZE);
        assertEquals(user, users.get(0), WRONG_USER);
    }
}
