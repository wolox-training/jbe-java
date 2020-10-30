package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_SIZE;
import static wolox.training.util.MessageConstants.WRONG_USER;
import static wolox.training.util.ParamsConstants.MAGIC_ID;

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
    public static final LocalDate START_DATE = LocalDate.of(1981, 12, 13);
    public static final LocalDate END_DATE = LocalDate.of(2000, 4, 5);
    public static final String NAME = "ua";

    @Test
    void whenCreateUser_ThenIsPersisted() {
        User user = MockTestEntities.mockNewUser();

        userRepository.save(user);
        Optional<User> optionalUser = userRepository.findById(MAGIC_ID);

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
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, END_DATE,
            NAME);
        assertEquals(1, users.size(), WRONG_SIZE);
        assertEquals(user, users.get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdatesAndContainingCharactersInNameWithNullStartdate_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(null, END_DATE, NAME);
        assertEquals(1, users.size(), WRONG_SIZE);
        assertEquals(user, users.get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdatesAndContainingCharactersInNameWithNullEnddate_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, null, NAME);
        assertEquals(1, users.size(), WRONG_SIZE);
        assertEquals(user, users.get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdatesAndContainingCharactersInNameWithNullName_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, END_DATE, null);
        assertEquals(1, users.size(), WRONG_SIZE);
        assertEquals(user, users.get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdatesAndContainingCharactersInNameWithNulls_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        List<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(null, null, null);
        assertEquals(1, users.size(), WRONG_SIZE);
    }
}
