package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_USER;

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
}
