package wolox.training.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ParamsConstants {

    public static final long MAGIC_ID = 1L;

    public static final String PUBLISHER = "Top Shelf Productions";
    public static final String GENRE = "Comics & graphic novels, general";
    public static final String YEAR = "2017";
    public static final String IMAGE = "No image";
    public static final String AUTHOR = "Alan Moore";
    public static final String PAGES = "56";
    public static final String TITLE = "Nemo";
    public static final String SUBTITLE = "Book Two";
    public static final String ISBN = "0385472579";

    public static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 20, Sort.unsorted());
}
