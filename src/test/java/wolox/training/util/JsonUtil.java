package wolox.training.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class JsonUtil {

    public static byte[] toJsonWithNulls(Object object) throws IOException {
        return toJson(object, true);
    }

    public static byte[] toJsonNonNulls(Object object) throws IOException {
        return toJson(object, false);
    }

    private static byte[] toJson(Object object, boolean includeNull) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        if (includeNull) mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
