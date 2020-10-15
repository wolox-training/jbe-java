package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import static wolox.training.utils.ErrorConstants.BOOK_ALREADY_OWNED;
import static wolox.training.utils.ErrorConstants.BOOK_NOT_FOUND;

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
        checkArgument(!Strings.isNullOrEmpty(username));
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkArgument(!Strings.isNullOrEmpty(name));
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = checkNotNull(birthdate);
        checkArgument(birthdate.isBefore(LocalDate.now()));
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public void addBook(Book book) {
        if (books.contains(checkNotNull(book))) {
            throw new BookAlreadyOwnedException(BOOK_ALREADY_OWNED);
        } else {
            this.books.add(book);
        }
    }

    public void removeBook(Book book) {
        if (books.contains(checkNotNull(book))) {
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
