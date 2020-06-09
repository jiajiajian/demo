package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TerminalCmd extends TerminalCmdBase {

    private int timeout;

    @Override
    public String toString() {
        return "TerminalCmd{" +
                "timeout=" + timeout +
                "} " + super.toString();
    }

}
