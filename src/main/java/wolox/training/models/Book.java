package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book {

    private String genre;

    @NotNull
    @Column(nullable = false)
    private String author;

    @NotNull
    @Column(nullable = false)
    private String image;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String subtitle;

    @NotNull
    @Column(nullable = false)
    private String publisher;

    @NotNull
    @Column(nullable = false)
    private String year;

    @NotNull
    @Column(nullable = false)
    private String pages;

    @NotNull
    @Column(nullable = false, unique = true)
    private String isbn;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
    private Long id;

    @ManyToMany(mappedBy = "books")
    private final List<User> users = new ArrayList<>();

    public Book() {}

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        checkArgument(!Strings.isNullOrEmpty(author));
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        checkArgument(!Strings.isNullOrEmpty(image));
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        checkArgument(!Strings.isNullOrEmpty(title));
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        checkArgument(!Strings.isNullOrEmpty(subtitle));
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        checkArgument(!Strings.isNullOrEmpty(publisher));
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        checkArgument(!Strings.isNullOrEmpty(year));
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        checkArgument(!Strings.isNullOrEmpty(pages));
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        checkArgument(!Strings.isNullOrEmpty(isbn));
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(genre, book.genre) &&
            author.equals(book.author) &&
            image.equals(book.image) &&
            title.equals(book.title) &&
            subtitle.equals(book.subtitle) &&
            publisher.equals(book.publisher) &&
            year.equals(book.year) &&
            pages.equals(book.pages) &&
            isbn.equals(book.isbn) &&
            id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre, author, image, title, subtitle, publisher, year, pages, isbn, id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "genre='" + genre + '\'' +
            ", author='" + author + '\'' +
            ", image='" + image + '\'' +
            ", title='" + title + '\'' +
            ", subtitle='" + subtitle + '\'' +
            ", publisher='" + publisher + '\'' +
            ", year='" + year + '\'' +
            ", pages='" + pages + '\'' +
            ", isbn='" + isbn + '\'' +
            ", id=" + id +
            '}';
    }
}
