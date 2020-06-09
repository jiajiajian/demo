package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TerminalCmdBase {
    private String terminalType;
    private String terminalID;
    private int cmdID;
    private int cmdSerial;
    private String cmdBody;
    private int cmdType;

    @Override
    public String toString() {
        return "TerminalCmdBase{" +
                "terminalType='" + terminalType + '\'' +
                ", terminalID='" + terminalID + '\'' +
                ", cmdID=" + cmdID +
                ", cmdSerial=" + cmdSerial +
                ", cmdBody='" + cmdBody + '\'' +
                ", cmdType=" + cmdType +
                '}';
    }
}
