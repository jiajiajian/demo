package cn.com.tiza.domain;
import java.math.*;
import java.util.Date;
import java.sql.Timestamp;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.AssignID;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
@Table(name="v_fence_vehicle")
public class FenceVehicle  implements Serializable {

	@AutoID
	@AssignID("simple")
    private Long id ;
    /**
     * 围栏id
     */
    private Long fenceId ;
    private String vin ;
	
    public FenceVehicle() {
    }

}
