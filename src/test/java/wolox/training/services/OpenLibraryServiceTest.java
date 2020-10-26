package wolox.training.services;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.BookDTO;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = OpenLibraryService.class)
@TestPropertySource("classpath:application.properties")
class OpenLibraryServiceTest {

    private static final String PATH = "/api/";

    @Autowired
    private OpenLibraryService openLibraryService;
    @Value("${successBookApiSubpath}")
    private String successBookApiSubpath;
    @Value("${failBookApiSubpath}")
    private String failedBookApiSubpath;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() throws IOException {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo(PATH + successBookApiSubpath))
            .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatus(200)
                .withBodyFile("zen_speaks.json")));

        wireMockServer.stubFor(get(urlEqualTo(PATH + failedBookApiSubpath))
            .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatus(200)
                .withBody("{}")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void whenFindBookByIsbn_ThenReturnBookDTO() {
        BookDTO bookDTO = openLibraryService.bookInfo("0385472579");

        assertNotNull(bookDTO);
        assertEquals("0385472579", bookDTO.getIsbn());
        assertEquals("Zen speaks", bookDTO.getTitle());
        assertEquals("1994", bookDTO.getPublishDate());
    }

    @Test
    void whenFindNoExistentBookByIsbn_ThenThrowException() {
        assertThrows(BookNotFoundException.class, () -> openLibraryService.bookInfo("077"));
    }
}
