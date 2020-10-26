package wolox.training.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO implements Serializable {

    private String isbn;
    private String title;
    private String subtitle;
    private List<HashMap<String, String>> publishers;
    private String publishDate;
    private String pageNumber;
    private List<HashMap<String, String>> authors;

    public Book toBook() {
        Book book = new Book();
        book.setImage("No image");
        book.setIsbn(this.isbn);
        book.setTitle(this.title);
        book.setSubtitle(this.subtitle);
        book.setPublisher(this.publishers.get(0).get("name"));
        book.setPages(this.pageNumber);
        book.setAuthor(this.authors.get(0).get("name"));
        book.setYear(this.publishDate);

        return book;
    }
}
