package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Getter
@Setter
public class FaultDictItemQuery extends Query {

    private String spnFmi;
    private String fmiName;
    private String spnName;
    private Long tlaId;

    @Override
    protected void convertParams() {
        add("spnFmi", this.spnFmi, true);
        add("fmiName", this.fmiName, true);
        add("spnName", this.spnName, true);
        add("tlaId", this.tlaId);
    }

}
