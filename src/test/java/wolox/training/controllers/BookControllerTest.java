package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doDelete;
import static wolox.training.util.MockMvcHttpRequests.doPut;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.security.CustomAuthenticationProvider;
import wolox.training.util.MockTestEntities;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    private static final String BASE_PATH = "/books";
    private static Book book;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        book = MockTestEntities.mockPersistedBook();
    }

    @Test
    void whenDeleteBook_ThenHttpStatus204() throws Exception {
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        doDelete(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateBookWithIdMismatch_ThenHttpStatus400() throws Exception {
        doPut(mockMvc, BASE_PATH + "/2", book)
            .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateBookWithoutBody_ThenHttpStatus404() throws Exception {
        doPut(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isBadRequest());
    }
}
