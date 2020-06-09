package cn.com.tiza.service.dto;

import cn.com.tiza.constant.GeneralParamEnum;
import lombok.Data;

/**
 * 终端参数设置
 *
 * @author villas
 */
@Data
public class TerminalParam {

    private GeneralParamEnum key;

    private String value;
}
