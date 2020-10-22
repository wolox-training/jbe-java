package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doGet;
import static wolox.training.util.MockMvcHttpRequests.doPost;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.security.CustomAuthenticationProvider;
import wolox.training.util.JsonUtil;
import wolox.training.util.MockTestEntities;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private static final long MAGIC_ID = 1L;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_PATH = "/users";
    private static User newUser;
    private static User persistedUser;

    @BeforeAll
    static void setUp() {
        newUser = MockTestEntities.mockNewUser();
        persistedUser = MockTestEntities.mockPersistedUser();
    }

    @Test
    void whenFindUserById_ThenHttpStatus200() throws Exception {
        given(userRepository.findById(MAGIC_ID)).willReturn(Optional.of(persistedUser));

        doGet(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isOk());
    }

    @Test
    void whenCreateUser_ThenHttpStatus201() throws Exception {
        doPost(mockMvc, BASE_PATH, newUser).
            andExpect(status().isCreated());
    }

    @Test
    void whenAddBookToUser_ThenHttpStatus204() throws Exception {
        given(userRepository.findById(MAGIC_ID)).willReturn(Optional.of(newUser));
        given(bookRepository.existsById(MAGIC_ID)).willReturn(true);

        mockMvc.perform(patch(BASE_PATH + "/1/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(MockTestEntities.mockPersistedBook())))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
