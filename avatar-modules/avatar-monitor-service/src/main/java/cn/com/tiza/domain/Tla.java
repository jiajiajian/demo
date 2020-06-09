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
* gen by beetlsql 2020-05-12
* @author tiza
*/
@Data
@Table(name="v_tla")
public class Tla  implements Serializable ,Entity{

	@AutoID
	@AssignID("simple")
    private Long id ;
    private Long createTime ;
    private String createUserAccount ;
    private Long organizationId ;
    private String tla ;
    private String tlaId ;
	
    public Tla() {
    }

}
