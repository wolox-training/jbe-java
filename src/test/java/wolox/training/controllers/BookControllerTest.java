package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.util.MockTestEntities;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    private static final String BASE_PATH = "/books";
    private static Book book;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        book = MockTestEntities.mockPersistedBook();
    }

    @Test
    void whenDeleteBook_ThenHttpStatus204() throws Exception {
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mockMvc.perform(delete(BASE_PATH + "/1"))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdateBookWithIdMismatch_ThenHttpStatus400() throws Exception {
        mockMvc.perform(put(BASE_PATH + "/2"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateBookWithoutBody_ThenHttpStatus404() throws Exception {
        mockMvc.perform(put(BASE_PATH + "/1"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
