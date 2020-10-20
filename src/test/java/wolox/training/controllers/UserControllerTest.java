package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.util.JsonUtil;
import wolox.training.util.MockTestEntities;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private static final long MAGIC_ID = 1L;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_PATH = "/users";
    private static User newUser;

    @BeforeAll
    static void setUp() {
        newUser = MockTestEntities.mockNewUser();
    }

    @Test
    void whenFindUserById_ThenHttpStatus200() throws Exception {
        given(userRepository.findById(MAGIC_ID)).willReturn(Optional.of(newUser));

        String response = mockMvc.perform(get(BASE_PATH + "/1").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Logger.getLogger(UserControllerTest.class.getName()).info(response);
    }

    @Test
    void whenCreateUser_ThenHttpStatus201() throws Exception {
        mockMvc.perform(post(BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(newUser)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    void whenAddBookToUser_ThenHttpStatus204() throws Exception {
        given(userRepository.findById(MAGIC_ID)).willReturn(Optional.of(newUser));
        given(bookRepository.existsById(MAGIC_ID)).willReturn(true);

        Book book = MockTestEntities.mockPersistedBook();

        mockMvc.perform(patch(BASE_PATH + "/1/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(book)))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
