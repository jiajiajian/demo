package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.Map;

/**
 * @author tz0920
 */
@Data
public class GeocodeVM {
    /**
     * 返回值为 0 或 1，0 表示请求失败；1 表示请求成功
     */
    private int status;
    private Map regeocode;
}
