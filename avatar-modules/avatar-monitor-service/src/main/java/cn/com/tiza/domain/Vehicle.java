package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.Table;

/**
 * @author tz0920
 */
@Data
@Table(name = "v_vehicle")
public class Vehicle {
    private Long id;
    private String vin;
    private Long organizationId;
}
