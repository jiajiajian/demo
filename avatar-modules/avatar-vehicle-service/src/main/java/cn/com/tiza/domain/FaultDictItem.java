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
* gen by beetlsql 2020-05-21
* @author tiza
*/
@Data
@Table(name="v_fault_dict_item")
public class FaultDictItem  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    private String fmi ;
    private String fmiName ;
    private Long organizationId ;
    private String spn ;
    private String spnName ;
    private Long tlaId ;
	
    public FaultDictItem() {
    }

}
