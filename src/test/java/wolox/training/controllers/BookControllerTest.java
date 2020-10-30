package wolox.training.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.util.MockMvcHttpRequests.doDelete;
import static wolox.training.util.MockMvcHttpRequests.doGet;
import static wolox.training.util.MockMvcHttpRequests.doPut;
import static wolox.training.util.ParamsConstants.AUTHOR;
import static wolox.training.util.ParamsConstants.ISBN;
import static wolox.training.util.ParamsConstants.MAGIC_ID;
import static wolox.training.util.ParamsConstants.TITLE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.BookDTO;
import wolox.training.repositories.BookRepository;
import wolox.training.security.CustomAuthenticationProvider;
import wolox.training.services.OpenLibraryService;
import wolox.training.util.JsonUtil;
import wolox.training.util.MockTestEntities;
import wolox.training.utils.ErrorConstants;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    private static final String BASE_PATH = "/books";
    private static Book book;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CustomAuthenticationProvider customAuthenticationProvider;
    @MockBean
    private OpenLibraryService openLibraryService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        book = MockTestEntities.mockPersistedBook();
    }

    @Test
    void whenDeleteBook_ThenHttpStatus204() throws Exception {
        given(bookRepository.findById(MAGIC_ID)).willReturn(Optional.of(book));

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

    @Test
    void whenFindBookById_ThenHttpStatus200() throws Exception {
        Book persistedBook = MockTestEntities.mockPersistedBook();
        given(bookRepository.findById(MAGIC_ID)).willReturn(Optional.of(persistedBook));

        doGet(mockMvc, BASE_PATH + "/1")
            .andExpect(status().isOk())
            .andExpect(content().string(new String(JsonUtil.toJsonNonNulls(persistedBook))));
    }

    @Test
    void whenFindBookByIsbn_ThenHttpStatus200() throws Exception {
        Book persistedOpenLibraryBook = MockTestEntities.mockPersistedOpenLibraryBook();

        given(bookRepository.findByIsbn(ISBN)).willReturn(Optional.of(persistedOpenLibraryBook));

        doGet(mockMvc, BASE_PATH + "?isbn=" + ISBN)
            .andExpect(status().isOk())
            .andExpect(content().string(new String(JsonUtil.toJsonNonNulls(persistedOpenLibraryBook))));
    }

    @Test
    void whenFindBookByIsbnThatNoExistInRepositories_ThenHttpStatus201() throws Exception {
        BookDTO bookDto = MockTestEntities.mockBookDTO();
        Book persistedOpenLibraryBook = MockTestEntities.mockPersistedOpenLibraryBook();
        Optional<Book> optionalBook = Optional.empty();

        given(bookRepository.findByIsbn(ISBN)).willReturn(optionalBook);
        when(openLibraryService.bookInfo(ISBN)).thenReturn(bookDto);
        when(bookRepository.save(any())).thenReturn(persistedOpenLibraryBook);

        Logger.getLogger(this.getClass().getName()).info(persistedOpenLibraryBook.toString());

        doGet(mockMvc, BASE_PATH + "?isbn=" + ISBN)
            .andExpect(status().isCreated())
            .andExpect(content().string(new String(JsonUtil.toJsonNonNulls(persistedOpenLibraryBook))));
    }

    @Test
    void whenFindBookByIsbnThatNoExist_ThenHttpStatus404() throws Exception {
        String isbn = "077";
        Book persistedOpenLibraryBook = MockTestEntities.mockPersistedOpenLibraryBook();
        Optional<Book> optionalBook = Optional.empty();

        given(bookRepository.findByIsbn(isbn)).willReturn(optionalBook);
        when(openLibraryService.bookInfo(isbn))
            .thenThrow(new BookNotFoundException(String.format(ErrorConstants.BOOK_ISBN_NOT_FOUND, isbn)));

        Logger.getLogger(this.getClass().getName()).info(persistedOpenLibraryBook.toString());

        doGet(mockMvc, BASE_PATH + "?isbn=" + isbn)
            .andExpect(status().isNotFound());
    }

    @Test
    void whenFindAllBooksFilteringByAuthor_ThenHttpStatus200() throws Exception {
        List<Book> books = MockTestEntities.mockBooks()
            .stream()
            .filter(b -> b.getAuthor().toUpperCase().equals(AUTHOR.toUpperCase()))
            .collect(Collectors.toList());

        given(bookRepository.findAll(AUTHOR, "", "", "", "", "", "", "")).willReturn(books);

        MvcResult result = doGet(mockMvc, BASE_PATH + "?author=" + AUTHOR)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[5].author").exists())
            .andExpect(jsonPath("$[5].author").value(AUTHOR))
            .andReturn();

        books = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Book>>() {
            });

        assertEquals(6, books.size());
    }

    @Test
    void whenFindAllBooksFilteringByTitle_ThenHttpStatus200() throws Exception {
        List<Book> books = MockTestEntities.mockBooks()
            .stream()
            .filter(b -> b.getTitle().toUpperCase().equals(TITLE.toUpperCase()))
            .collect(Collectors.toList());

        given(bookRepository.findAll("", "", "", "", "", "", TITLE, "")).willReturn(books);

        MvcResult result = doGet(mockMvc, BASE_PATH + "?title=" + TITLE)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[1].title").exists())
            .andExpect(jsonPath("$[1].title").value(TITLE))
            .andReturn();

        books = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Book>>() {
            });

        assertEquals(2, books.size());
    }
}
