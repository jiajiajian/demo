package cn.com.tiza.cmd.command.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-05-14
 *
 * @author tiza
 */
@Data
@Table(name = "t_command")
public class Command implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 指令值10进制
     */
    private Integer cmdId;
    /**
     * 指令下发的时间格式yyyyMMdd
     */
    private Integer date;
    /**
     * 指令序列号
     */
    private Integer serialNo;
    /**
     * 状态 0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送 5: 其他 7：指令已发送终端
     */
    private Integer state;
    /**
     * 请求的报文数据
     */
    private String reqBody;
    /**
     * 响应的报文（base64加密）
     */
    private String resBody;
    /**
     * 响应的json报文
     */
    private String resJsonBody;
    /**
     * 指令类型 0：实时指令，1离线指令
     */
    private Integer cmdType;
    /**
     * 原因
     */
    private String reason;
    /**
     * 备注、其他
     */
    private String remark;
    /**
     * 指令响应时间
     */
    private Long resTime;
    /**
     * 指令发送时间
     */
    private Long sendTime;
    /**
     * vin码
     */
    private String vin;

    private Long orgId;

    private Long operateTime;

    private Long userId;

    private String ipAddress;

    public Command() {
    }

    /*-----------not in db---------------*/

    private String itemName;

    private String username;
}
