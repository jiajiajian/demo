package cn.com.tiza.domain;

import cn.com.tiza.service.dto.BusinessLogType;
import lombok.Builder;
import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-17
 *
 * @author tiza
 */
@Data
@Table(name = "v_business_log")
@Builder
public class BusinessLog implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    private Long organizationId;
    /**
     * 操作类型
     */
    private BusinessLogType operateType;
    private Long createTime;
    private String createUserAccount;
    private String createUserRealname;
    /**
     * 新的SIM卡号
     */
    private String simcard;
    /**
     * 新的终端编号
     */
    private String terminal;
    private String vin;
    /**
     * 旧的SIM卡号
     */
    private String oldSimcard;
    /**
     * 旧的终端编号
     */
    private String oldTerminal;
    private String oldVin;
    private Integer renewalMon;

}
