package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Rest create a history data querier result
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestCreateDataQueryResult extends RestResult {

    /**
     * History data query id
     */
    Long queryId;

    public RestCreateDataQueryResult() {
    }

    public RestCreateDataQueryResult(Boolean isSuccess, Integer errorCode) {
        super(isSuccess, errorCode);
    }
}
