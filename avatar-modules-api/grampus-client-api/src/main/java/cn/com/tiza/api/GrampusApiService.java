package cn.com.tiza.api;

/**
 * 查询Api Key
 * @author tiza
 */
public interface GrampusApiService {

    /**
     * 根据终端类型查询api key
     * @param terminalType
     * @return
     */
    String getApiKey(String terminalType);

}
