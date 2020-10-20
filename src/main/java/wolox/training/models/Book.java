package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static wolox.training.utils.ErrorConstants.INVALID_ISBN;
import static wolox.training.utils.ErrorConstants.INVALID_PAGES;
import static wolox.training.utils.ErrorConstants.INVALID_YEAR;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_AUTHOR_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_IMAGE_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_ISBN_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_PAGES_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_PUBLISHER_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_SUBTITLE_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_TITLE_FIELD;
import static wolox.training.utils.ErrorConstants.OBLIGATORY_YEAR_FIELD;
import static wolox.training.utils.ErrorConstants.PAGES_LESS_THAN_ZERO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * This class represent a book and its attributes
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Book {

    @Getter
    @Setter
    private String genre;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String author;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String image;


    @Getter
    @NotNull
    @Column(nullable = false)
    private String title;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String subtitle;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String publisher;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String year;

    @Getter
    @NotNull
    @Column(nullable = false)
    private String pages;

    @Getter
    @NotNull
    @Column(nullable = false, unique = true)
    private String isbn;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
    @Getter
    private Long id;

    @ManyToMany(mappedBy = "books")
    private final List<User> users = new ArrayList<>();

    public void setAuthor(String author) {
        checkArgument(!Strings.isNullOrEmpty(author), OBLIGATORY_AUTHOR_FIELD);
        this.author = author;
    }

    public void setImage(String image) {
        checkArgument(!Strings.isNullOrEmpty(image), OBLIGATORY_IMAGE_FIELD);
        this.image = image;
    }

    public void setTitle(String title) {
        checkArgument(!Strings.isNullOrEmpty(title), OBLIGATORY_TITLE_FIELD);
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        checkArgument(!Strings.isNullOrEmpty(subtitle), OBLIGATORY_SUBTITLE_FIELD);
        this.subtitle = subtitle;
    }

    public void setPublisher(String publisher) {
        checkArgument(!Strings.isNullOrEmpty(publisher), OBLIGATORY_PUBLISHER_FIELD);
        this.publisher = publisher;
    }

    public void setYear(String year) {
        checkArgument(!Strings.isNullOrEmpty(year), OBLIGATORY_YEAR_FIELD);
        checkArgument(StringUtils.isNumeric(year), INVALID_YEAR);
        this.year = year;
    }

    public void setPages(String pages) {
        checkArgument(!Strings.isNullOrEmpty(pages), OBLIGATORY_PAGES_FIELD);
        checkArgument(StringUtils.isNumeric(pages), INVALID_PAGES);
        checkArgument(Integer.parseInt(pages) > 0, PAGES_LESS_THAN_ZERO);
        this.pages = pages;
    }

    public void setIsbn(String isbn) {
        checkArgument(!Strings.isNullOrEmpty(isbn), OBLIGATORY_ISBN_FIELD);
        checkArgument(StringUtils.isNumeric(isbn), INVALID_ISBN);
        this.isbn = isbn;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }
}
