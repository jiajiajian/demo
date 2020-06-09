package cn.com.tiza.web.rest.util;

import org.beetl.sql.core.engine.PageQuery;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">GitHub API</a>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * @author tiza
 */
public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(PageQuery<T> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalRow()));
        return headers;
    }

}
