package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doGet;
import static wolox.training.util.MockMvcHttpRequests.doPost;
import static wolox.training.util.MockMvcHttpRequests.doPut;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.security.CustomAuthenticationProvider;
import wolox.training.util.MockTestEntities;

@WebMvcTest(UserController.class)
class SecuredUserControllerTest {

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
    void whenCreateUserWithoutAuthentication_ThenIsPersistedAndHttpStatus201() throws Exception {
        doPost(mockMvc, BASE_PATH, newUser)
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void whenFindAllUsers_ThenHttpStatus200() throws Exception {
        doGet(mockMvc, BASE_PATH)
            .andExpect(status().isOk());
    }

    @Test
    void whenFindAllUsersWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doGet(mockMvc, BASE_PATH)
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    void whenFindUserByIdWithoutAuthentication_ThenHttpStatus401() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(persistedUser));

        doGet(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    void whenGetAuthenticatedUserWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doGet(mockMvc, BASE_PATH + "/currentUser")
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("spring")
    void whenGetAuthenticatedUser_ThenHttpStatus200() throws Exception {
        doGet(mockMvc, BASE_PATH + "/currentUser")
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"user\": \"spring\"}"));
    }

    @Test
    void whenUpdateUserWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doPut(mockMvc, BASE_PATH + "/1", persistedUser)
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    void whenUpdatePasswordWithoutAuthentication_ThenHttpStatus401() throws Exception {
        doPut(mockMvc, BASE_PATH + "/1/password", persistedUser)
            .andExpect(status().isUnauthorized())
            .andExpect(header().exists("WWW-Authenticate"));
    }

    @Test
    @WithMockUser
    void whenUpdatePassword_ThenHttpStatus204() throws Exception {
        given(userRepository.existsById(1L)).willReturn(true);
        persistedUser.setPassword("1234567890");

        doPut(mockMvc, BASE_PATH + "/1/password", persistedUser)
            .andExpect(status().isNoContent());
    }
}
