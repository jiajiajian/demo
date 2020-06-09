package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Getter
@Setter
public class LockApplyQuery extends Query {

    private Integer state;

    private String applyCode;

    private Long applyId;

    private String instanceId;

    private Long beginTime;

    private Long endTime;

    private String applyUser;

    @Override
    protected void convertParams() {
        add("state", this.state);
        add("applyCode", this.applyCode, true);
        add("applyId", this.applyId);
        add("instanceId",this.instanceId);
        add("state",this.state);
        add("applyUser",this.applyUser);
        add("beginTime",this.beginTime);
        add("endTime",this.endTime);
    }

}
