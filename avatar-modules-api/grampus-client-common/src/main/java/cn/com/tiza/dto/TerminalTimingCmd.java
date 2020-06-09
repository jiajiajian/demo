package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TerminalTimingCmd extends TerminalCmd {

    private String executeTime;

    @Override
    public String toString() {
        return "TerminalTimingCmd{" +
                "executeTime='" + executeTime + '\'' +
                "} " + super.toString();
    }

}
