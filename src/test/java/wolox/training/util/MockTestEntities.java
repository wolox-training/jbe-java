package wolox.training.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import wolox.training.models.Book;
import wolox.training.models.BookDTO;
import wolox.training.models.User;

/**
 * Class to mock the objects of the necessary entities in the tests
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class MockTestEntities {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Mock a persisted book object in a database
     *
     * @return new Book with id 1L
     */
    public static Book mockPersistedBook() {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();

        objectNode.put("id", 1);
        objectNode.put("author", "Yuval Noah Harari");
        objectNode.put("title", "Sapiens");
        objectNode.put("subtitle", "A Brief History of Humankind");
        objectNode.put("image", "sapiens.png");
        objectNode.put("isbn", "9780099590088");
        objectNode.put("pages", "498");
        objectNode.put("year", "2014");
        objectNode.put("publisher", "DEBATE");

        return OBJECT_MAPPER.convertValue(objectNode, Book.class);
    }

    /**
     * Mock a new book object to be persisted
     *
     * @return new book
     */
    public static Book mockNewBook() {
        Book newBook = new Book();
        newBook.setAuthor("Yuval Noah Harari");
        newBook.setTitle("Homo Deus");
        newBook.setSubtitle("A Brief History of tomorrow");
        newBook.setImage("deus.jpg");
        newBook.setIsbn("9780099590088");
        newBook.setPages("496");
        newBook.setYear("2016");
        newBook.setPublisher("DEBATE");
        newBook.setGenre("Natural History");

        return newBook;
    }

    /**
     * Mock a persisted user object in a database
     *
     * @return new user with id 1L
     */
    public static User mockPersistedUser() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();

        objectNode.put("id", 1);
        objectNode.put("name", "Samatha Ortega");
        objectNode.put("username", "samanthao");
        objectNode.put("birthdate", "1993-01-01");
        objectNode.put("password", "prueba");

        return OBJECT_MAPPER.convertValue(objectNode, User.class);
    }

    /**
     * Mock a new user object to be persisted
     *
     * @return new user
     */
    public static User mockNewUser() {
        User user = new User();
        user.setBirthdate(LocalDate.of(1983, 2, 15));
        user.setName("Juan David Celedon");
        user.setUsername("juandc");
        user.setPassword("testing");
        return user;
    }

    /**
     * Mock a returned BookDTO from OpenLibraryService
     *
     * @return a new BookDTO
     */
    public static BookDTO mockBookDTO() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("0385472579");
        bookDTO.setPageNumber("159");
        bookDTO.setTitle("Zen speaks");
        bookDTO.setSubtitle("shouts of nothingness");
        bookDTO.setPublishDate("1994");

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Anchor Books");

        bookDTO.setPublishers(Collections.singletonList(map));

        map.clear();
        map.put("name", "Zhizhong Cai");

        bookDTO.setAuthors(Collections.singletonList(map));

        return bookDTO;
    }

    /**
     * Mock a persisted book object in a database
     *
     * @return new Book with id 1L
     */
    public static Book mockPersistedOpenLibraryBook() throws JsonProcessingException {
        ObjectNode objectNode = OBJECT_MAPPER.valueToTree(mockBookDTO().toBook());
        objectNode.put("id", 1);

        Logger.getLogger(MockTestEntities.class.getName()).info(objectNode.asText());

        return OBJECT_MAPPER.convertValue(objectNode, Book.class);
    }

    /**
     * Mock a JSON response from OpenLibrary API
     *
     * @return JSON String
     * @throws IOException If JSON file not found
     */
    public static String mockOpenLibraryResponse() throws IOException {
        return Files.readAllLines(Paths.get("src", "test", "resources", "__files", "zen_speaks.json"))
            .stream().collect(Collectors.joining());
    }
}
