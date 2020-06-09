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
public class CmdDebugQuery extends Query {

    private Long vehicleTypeId;

    @Override
    protected void convertParams() {
        add("vehicleTypeId", this.vehicleTypeId);
    }

}
