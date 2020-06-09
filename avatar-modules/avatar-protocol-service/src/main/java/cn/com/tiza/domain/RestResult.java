package cn.com.tiza.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Base rest result
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResult {

    /**
     * Interface returns,if success,then true,else false
     */
    Boolean isSuccess;

    /**
     * If isSuccess is false,interface also will return errorCode
     */
    Integer errorCode;
}
