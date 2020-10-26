package wolox.training.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.BookDTO;
import wolox.training.utils.ErrorConstants;

@Service
public class OpenLibraryService {

    public static final String BOOKS_URI_PATH = "books";
    public static final String ISBN_QUERY_PARAM = "bibkeys";
    public static final String AUTHORS_ATTRIBUTE = "authors";
    public static final String PAGES_NUMBER_ATTRIBUTE = "number_of_pages";
    public static final String PUBLISHED_DATE_ATTRIBUTE = "publish_date";
    public static final String PUBLISHERS_ATTRIBUTE = "publishers";
    public static final String SUBTITLE_ATTRIBUTE = "subtitle";
    public static final String TITLE_ATTRIBUTE = "title";

    private final RestTemplate restTemplate;

    public OpenLibraryService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Value("${openLibraryUrl}")
    private String openLibraryUrl;

    public BookDTO bookInfo(String isbn) {
        final String isbnQueryValue = "ISBN:" + isbn;
        URI uri = UriComponentsBuilder
            .fromHttpUrl(openLibraryUrl)
            .path(BOOKS_URI_PATH)
            .queryParam(ISBN_QUERY_PARAM, isbnQueryValue)
            .queryParam("format", "json")
            .queryParam("jscmd", "data")
            .build().toUri();

        ObjectNode objectNode = restTemplate.getForObject(uri, ObjectNode.class);
        objectNode = objectNode == null ? null : (ObjectNode) objectNode.get(isbnQueryValue);

        if (objectNode != null && !objectNode.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();

            BookDTO bookDTO = new BookDTO();
            bookDTO.setIsbn(isbn);
            bookDTO.setAuthors(mapper.convertValue(objectNode.get(AUTHORS_ATTRIBUTE), List.class));
            bookDTO.setTitle(mapper.convertValue(objectNode.get(TITLE_ATTRIBUTE), String.class));
            bookDTO.setSubtitle(mapper.convertValue(objectNode.get(SUBTITLE_ATTRIBUTE), String.class));
            bookDTO.setPublishDate(mapper.convertValue(objectNode.get(PUBLISHED_DATE_ATTRIBUTE), String.class));
            bookDTO.setPublishers(mapper.convertValue(objectNode.get(PUBLISHERS_ATTRIBUTE), List.class));
            bookDTO.setPageNumber(mapper.convertValue(objectNode.get(PAGES_NUMBER_ATTRIBUTE), String.class));

            return bookDTO;
        } else {
            throw new BookNotFoundException(String.format(ErrorConstants.BOOK_ISBN_NOT_FOUND, isbn));
        }
    }
}
