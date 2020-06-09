package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * -
 *
 * @author villas
 */
@Getter
@Setter
public class VehicleQuery extends Query {

    private String vin;

    private String tid;

    private String plateNumber;

    private List<Long> jobIds;

    private String keyword;
    private Long taskId;

    @Override
    protected void convertParams() {
        add("vin", this.vin, true);
        add("tid", this.tid, true);
        add("plateNumber", this.plateNumber, true);
        add("jobIds", this.jobIds);
        add("keyword", this.keyword, true);
        add("taskId", this.taskId);
    }

}
