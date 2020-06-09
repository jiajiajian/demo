package cn.com.tiza.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Rest register terminal POST body
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TerminalCommand {

    String terminalID;

    /**
     * Terminal register time stamp
     */
    long registerTime;

    /**
     * The description of a terminal
     */
    String desc;

    public TerminalCommand() {}

    public TerminalCommand(String terminalID) {
        this.terminalID = terminalID;
        this.registerTime = System.currentTimeMillis();
    }
}
