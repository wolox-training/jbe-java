package wolox.training.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doDelete;
import static wolox.training.util.MockMvcHttpRequests.doGet;
import static wolox.training.util.MockMvcHttpRequests.doPost;
import static wolox.training.util.MockMvcHttpRequests.doPut;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.security.CustomAuthenticationProvider;
import wolox.training.util.MockTestEntities;

@WebMvcTest(BookController.class)
class SecuredBooknControllerTest {

    private static final String BASE_PATH = "/books";
    private static Book persistedBook;
    private static Book newBook;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        persistedBook = MockTestEntities.mockPersistedBook();
        newBook = MockTestEntities.mockNewBook();
    }

    @Test
    void whenCreateBookWithoutAuthentication_ThenHttpStatus201() throws Exception {
        doPost(mockMvc, BASE_PATH, newBook)
            .andExpect(status().isCreated());
    }

    @Test
    void whenFindBookByIdWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doGet(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    void whenDeleteBookWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doDelete(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    void whenUpdateBookWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doPut(mockMvc, BASE_PATH + "/1", persistedBook)
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }
}
