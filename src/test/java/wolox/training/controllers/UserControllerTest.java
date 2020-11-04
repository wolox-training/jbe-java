package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doGet;
import static wolox.training.util.MockMvcHttpRequests.doPost;
import static wolox.training.util.MockMvcHttpRequests.doPut;
import static wolox.training.util.ParamsConstants.DEFAULT_PAGEABLE;
import static wolox.training.util.ParamsConstants.MAGIC_ID;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static List<User> users;

    @BeforeAll
    static void setUp() throws Exception {
        newUser = MockTestEntities.mockNewUser();
        persistedUser = MockTestEntities.mockPersistedUser();
        users = MockTestEntities.mockUsers();
    }

    @Test
    void whenFindAllUsersWithPagination_ThenHttpStatus200() throws Exception {
        given(userRepository.findAll(DEFAULT_PAGEABLE))
            .willReturn(new PageImpl<>(users.subList(0, 20), DEFAULT_PAGEABLE, users.size()));

        doGet(mockMvc, BASE_PATH + "?page=0&size=20")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(20))
            .andExpect(jsonPath("$.numberOfElements").value(20))
            .andExpect(jsonPath("$.totalElements").value(100));
    }

    @Test
    void whenFindAllUsersWithPaginationAndSorting_ThenHttpStatus200() throws Exception {
        Pageable pageable = PageRequest.of(0, 30, Sort.by("username").ascending());
        List<User> sublist = users.stream()
            .sorted(Comparator.comparing(User::getUsername))
            .collect(Collectors.toList())
            .subList(0, 30);

        given(userRepository.findAll(pageable))
            .willReturn(new PageImpl<>(sublist, pageable, users.size()));

        doGet(mockMvc, BASE_PATH + "?page=0&size=30&sort=username&username.dir=asc")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.content[2].username").value("areicharz20"))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(30))
            .andExpect(jsonPath("$.numberOfElements").value(30))
            .andExpect(jsonPath("$.totalElements").value(100));
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
            .content(JsonUtil.toJsonWithNulls(MockTestEntities.mockPersistedBook())))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void whenUpdatePassword_ThenHttpStatus204() throws Exception {
        given(userRepository.existsById(MAGIC_ID)).willReturn(true);
        persistedUser.setPassword("1234567890");

        doPut(mockMvc, BASE_PATH + "/1/password", persistedUser)
            .andExpect(status().isNoContent());
    }
}
