package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static wolox.training.utils.ErrorConstants.BOOK_ALREADY_OWNED;
import static wolox.training.utils.ErrorConstants.BOOK_NOT_FOUND;
import static wolox.training.utils.ErrorConstants.BOOK_NOT_NULL;
import static wolox.training.utils.ErrorConstants.INVALID_BIRTHDATE;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_BIRTHDATE_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_NAME_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_USERNAME_FIELD;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Strings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;

/**
 * This class represent a user and its attributes
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "t_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthdate;

    @NotNull
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Book> books = new ArrayList<>();

    public User() {}

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        checkArgument(!Strings.isNullOrEmpty(username), OBLIGATORY_USERNAME_FIELD);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkArgument(!Strings.isNullOrEmpty(name), OBLIGATORY_NAME_FIELD);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        checkNotNull(birthdate, OBLIGATORY_BIRTHDATE_FIELD);
        checkArgument(birthdate.isBefore(LocalDate.now()), INVALID_BIRTHDATE);
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    /**
     * Add a book to the user's book collection
     * @param book must not be null
     * @throws NullPointerException if book is null
     * @throws BookAlreadyOwnedException if user already has the book
     */
    public void addBook(Book book) {
        if (books.contains(checkNotNull(book, BOOK_NOT_NULL))) {
            throw new BookAlreadyOwnedException(BOOK_ALREADY_OWNED);
        } else {
            this.books.add(book);
        }
    }

    /**
     * Remove a book from the user's book collection
     * @param book must not be null
     * @throws NullPointerException if book is null
     * @throws BookNotFoundException if user does not have the book to be removed
     */
    public void removeBook(Book book) {
        if (books.contains(checkNotNull(book, BOOK_NOT_NULL))) {
            this.books.remove(book);
        } else {
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND, book.getId()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id.equals(user.id) &&
            username.equals(user.username) &&
            name.equals(user.name) &&
            birthdate.equals(user.birthdate) &&
            books.equals(user.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, birthdate, books);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", name='" + name + '\'' +
            ", birthdate=" + birthdate +
            ", books=" + books +
            '}';
    }
}
