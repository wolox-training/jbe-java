package wolox.training.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import wolox.training.models.Book;
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
        objectNode.put("publisher", "DEBAT");

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
        return user;
    }
}
