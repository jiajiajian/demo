package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CmdCheckResult extends RestResult {

    private int cmdStatus;

    private Long cmdReplyTime;

    private String cmdReplyBody;

    private String cmdCheckId;

    public CmdCheckResult() {
    }

    public CmdCheckResult(Boolean isSuccess, Integer errorCode) {
        super(isSuccess, errorCode);
    }
}
