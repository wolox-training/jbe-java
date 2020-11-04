package wolox.training.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wolox.training.util.MessageConstants.EXCEPTION_THROWN;
import static wolox.training.util.MessageConstants.WRONG_PAGE;
import static wolox.training.util.MessageConstants.WRONG_PAGE_SIZE;
import static wolox.training.util.MessageConstants.WRONG_SIZE;
import static wolox.training.util.MessageConstants.WRONG_USER;
import static wolox.training.util.ParamsConstants.DEFAULT_PAGEABLE;
import static wolox.training.util.ParamsConstants.MAGIC_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    void whenFindAllUsersBetweenTwoBirthdaysAndContainingCharactersInName_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        Page<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, END_DATE,
            NAME, DEFAULT_PAGEABLE);
        assertEquals(1, users.getContent().size(), WRONG_SIZE);
        assertEquals(user, users.getContent().get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdaysAndContainingCharactersInNameWithNullStartdate_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        Page<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(null, END_DATE, NAME,
            DEFAULT_PAGEABLE);
        assertEquals(1, users.getContent().size(), WRONG_SIZE);
        assertEquals(user, users.getContent().get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdaysAndContainingCharactersInNameWithNullEndDate_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        Page<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, null, NAME,
            DEFAULT_PAGEABLE);
        assertEquals(1, users.getContent().size(), WRONG_SIZE);
        assertEquals(user, users.getContent().get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdaysAndContainingCharactersInNameWithNullName_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        Page<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(START_DATE, END_DATE,
            null, DEFAULT_PAGEABLE);
        assertEquals(1, users.getContent().size(), WRONG_SIZE);
        assertEquals(user, users.getContent().get(0), WRONG_USER);
    }

    @Test
    void whenFindAllUsersBetweenTwoBirthdaysAndContainingCharactersInNameWithNulls_ThenReturnList() {
        User user = MockTestEntities.mockNewUser();
        userRepository.saveAndFlush(user);

        Page<User> users = userRepository.findAllByBirthdateBetweenAndNameContainingIgnoreCase(null, null, null,
            DEFAULT_PAGEABLE);
        assertEquals(1, users.getContent().size(), WRONG_SIZE);
    }

    @Test
    void whenFindAllUsersWithPagination_ThenReturnPage() throws Exception {
        userRepository.saveAll(MockTestEntities.mockUsers());

        Page<User> users = userRepository.findAll(PageRequest.of(2, 20));
        assertEquals(2, users.getNumber(), WRONG_PAGE);
        assertEquals(20, users.getSize(), WRONG_PAGE_SIZE);
        assertEquals(20, users.getContent().size(), WRONG_SIZE);
        assertEquals(100, users.getTotalElements(), WRONG_SIZE);
    }

    @Test
    void whenFindAllUsersWithPaginationAndSorting_ThenReturnPage() throws Exception {
        userRepository.saveAll(MockTestEntities.mockUsers());

        Page<User> users = userRepository.findAll(PageRequest.of(0, 10, Sort.by("birthdate").descending()));
        assertEquals(0, users.getNumber(), WRONG_PAGE);
        assertEquals(10, users.getSize(), WRONG_PAGE_SIZE);
        assertEquals(100, users.getTotalElements(), WRONG_SIZE);

        User user = users.getContent().get(0);

        assertEquals("rmeads1p", user.getUsername());
        assertEquals("Ruthanne Meads", user.getName());
        assertEquals(LocalDate.of(2000,10,6), user.getBirthdate());
    }
}
