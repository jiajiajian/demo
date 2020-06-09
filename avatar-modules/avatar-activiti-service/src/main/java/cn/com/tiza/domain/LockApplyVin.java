package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Data
@Table(name = "r_lock_apply_vin")
public class LockApplyVin implements Serializable {

    /**
     * 申请单号id
     */
    private Long applyId;
    /**
     * VIN码
     */
    private String vin;

    public LockApplyVin() {
    }

    public LockApplyVin(Long applyId, String vin) {
        this.applyId = applyId;
        this.vin = vin;
    }
}
