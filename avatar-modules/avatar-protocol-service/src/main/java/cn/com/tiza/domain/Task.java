package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;

/**
 * 
 * @author villas
 */
@Data
@Table(name = "FWP_TASK")
public class Task implements Serializable {
    @AssignID("simple")
    private Long id;
    private Integer batteryDataFlag;
    private String createIpAddress;
    private Long createTime;
    private String createUserAccount;
    private String createUserRealname;
    private Integer customDataFlag;
    private String description;
    private String encrypt;
    private String idcode;
    private String ip;
    private Integer linkAmount;
    private String linkType;
    private Integer loginStatus;
    private String name;
    private Integer port;
    private Long relationId;
    private Long vehicleAmount;
    private Integer activeStatus;
}
