package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Rest history data query result
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestDataQueryResult extends RestResult {

    /**
     * The data query result is reach end ?
     */
    Boolean isEnd;

    /**
     * Result data records
     */
    List<RestDataRecord> records;

    public RestDataQueryResult() {
    }

    public RestDataQueryResult(Boolean isSuccess, Integer errorCode) {
        super(isSuccess, errorCode);
    }
}
