package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Data
@Table(name = "t_terminal_test")
public class TerminalTest implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;

    private String terminalCode;
    /**
     * 指令表id
     */
    private Long commandId;

    private String code;

    public TerminalTest() {
    }

    public TerminalTest(String terminalCode, Long commandId) {
        this.terminalCode = terminalCode;
        this.commandId = commandId;
    }
    /*------------not in db--------------------*/

    private Integer cmdId;

    private Integer state;

    private String result;

    private String content;

    private String itemKey;

    private Long testTime;

    public String getResult() {
        return this.state == null ? "" : CmdRes.getStatus(this.state);
    }

}
