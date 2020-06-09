package cn.com.tiza.service;

import cn.com.tiza.api.GrampusApiClient;
import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.dto.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tiza
 */
@Slf4j
@Service
public class GrampusTerminalService {

    @Autowired
    private GrampusApiClient grampusApiClient;

    @Autowired
    private GrampusApiService grampusApiService;

    public RestResult register(String terminalType, String terminalId) {
        String apiKey = this.grampusApiService.getApiKey(terminalType);
        return grampusApiClient.register(apiKey, terminalType, terminalId);
    }

    public RestResult unregister(String terminalType, String terminalId) {
        String apiKey = this.grampusApiService.getApiKey(terminalType);
        return grampusApiClient.unregister(apiKey, terminalType, terminalId);
    }

}
