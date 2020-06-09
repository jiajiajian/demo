package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CmdSendResult extends RestResult {

    private String cmdCheckId;

    public CmdSendResult() {
    }

    public CmdSendResult(Boolean isSuccess, Integer errorCode) {
        super(isSuccess, errorCode);
    }
}
