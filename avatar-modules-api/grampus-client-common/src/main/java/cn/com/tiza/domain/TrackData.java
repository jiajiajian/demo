package cn.com.tiza.domain;

import cn.com.tiza.DataConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author tiza
 */
@Data
public class TrackData {

    @JsonProperty(DataConstants.TIME)
    private Long time;

    @JsonProperty(DataConstants.VIN)
    private String vin;

    @JsonProperty(DataConstants.MDT_PO_DA_TYPE)
    private LocationData pi;

    @JsonProperty(DataConstants.MDT_EN_DA_GB6_TYPE)
    private EngineData ed;

}
