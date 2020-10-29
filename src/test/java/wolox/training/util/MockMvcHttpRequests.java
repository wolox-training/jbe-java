package wolox.training.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * This class contains the regular methods for HTTP requests
 *
 * @author Juan David Bermudez
 * @version 1.0
 * @since 1.0
 */
public class MockMvcHttpRequests {

    public static ResultActions doGet(MockMvc mockMvc, String path) throws Exception {
        return mockMvc.perform(get(path)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    /**
     * Do a POST request without body
     * @param mockMvc must no be null
     * @param path must no be null
     * @return Actions for test expectations
     * @throws Exception
     */
    public static ResultActions doPost(MockMvc mockMvc, String path) throws Exception {
        return mockMvc.perform(post(path)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    /**
     * Do a POST request with JSON body
     * @param mockMvc must no be null
     * @param path must no be null
     * @return Actions for test expectations
     * @throws Exception
     */
    public static ResultActions doPost(MockMvc mockMvc, String path, Object body) throws Exception {
        return mockMvc.perform(post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJsonWithNulls(body)))
            .andDo(print());
    }

    /**
     * Do a PUT request without body
     * @param mockMvc must no be null
     * @param path must no be null
     * @return Actions for test expectations
     * @throws Exception
     */
    public static ResultActions doPut(MockMvc mockMvc, String path) throws Exception {
        return mockMvc.perform(put(path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    /**
     * Do a PUT request with JSON body
     * @param mockMvc must no be null
     * @param path must no be null
     * @return Actions for test expectations
     * @throws Exception
     */
    public static ResultActions doPut(MockMvc mockMvc, String path, Object body) throws Exception {
        return mockMvc.perform(put(path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJsonWithNulls(body)))
            .andDo(print());
    }

    public static ResultActions doDelete(MockMvc mockMvc, String path) throws Exception {
        return mockMvc.perform(delete(path)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }
}
