package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Rest history data record
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestDataRecord {

    /**
     * Data time
     */
    long time;

    /**
     * Data command id
     */
    Integer cmdID;

    /**
     * Data body
     */
    String body;

    ForwardJsonBody jsonBody;

    /**
     * 接收时间
     */
    Long rt;
}
