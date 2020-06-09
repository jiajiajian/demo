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
@Table(name = "FWP_VEHICLE_DATA")
public class VehicleData implements Serializable {
    @AssignID("simple")
    private Long id;
    private Long createAt;
    private Long dataAmount;
    private Long errorDataAmount;
    private Long replyFaiAmount;
    private Long timeoutFaiAmount;
    private Long platResendAmount;
    private Long terminalResendAmount;
    private Long protocolId;
    private Long revAmount;
    private Long sucAmount;
    private Long updateAt;
    private String vin;
    private Long taskId;
    private Long statisticalTime;
    /**
     * 以下是非数据库字段，为了用于查询和其他表关联的字段映射
     */
    private String plateCode;

    private String orgName;

    private String address;

    private String province;

    private String city;

    private Long vehicleAmount;

    private Long faiAmount;

    private Long resendAmount;
}
