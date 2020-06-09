package cn.com.tiza.domain;

import cn.com.tiza.DataConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 国六发动机数据
 * @author tiza
 */
@Data
public class EngineData {

    @JsonProperty(DataConstants.MDT_EN_GB6_SPEED)
    private String speed;
    @JsonProperty(DataConstants.MDT_EN_GB6_ROTATE_SPEED)
    private String rotateSpeed;
    @JsonProperty(DataConstants.MDT_EN_GB6_DPF_DIFF)
    private String dpf;
    @JsonProperty(DataConstants.MDT_EN_GB6_TORQUE_OUT)
    private String torqueOut;
    @JsonProperty(DataConstants.MDT_EN_GB6_TORQUE_RUB)
    private String torqueRub;
    @JsonProperty(DataConstants.MDT_EN_GB6_MILEAGE)
    private String mileage;
    @JsonProperty(DataConstants.MDT_EN_GB6_SCR_TEM_IN)
    private String scrTemIn;
    @JsonProperty(DataConstants.MDT_EN_GB6_SCR_TEM_OUT)
    private String scrTemOut;
    @JsonProperty(DataConstants.MDT_EN_GB6_SCR_NOX_UP)
    private String noxUp;
    @JsonProperty(DataConstants.MDT_EN_GB6_SCR_NOX_DOWN)
    private String noxDown;

    private float nox;
}
