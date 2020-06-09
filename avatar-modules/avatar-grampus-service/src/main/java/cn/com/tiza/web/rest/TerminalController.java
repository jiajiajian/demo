package cn.com.tiza.web.rest;

import cn.com.tiza.dto.RestResult;
import cn.com.tiza.grampus.GrampusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 终端注册，删除
 * @author tiza
 */
@RestController
@RequestMapping("terminals")
public class TerminalController {

    @Autowired
    private GrampusClient grampusClient;

    @PostMapping("/{terminalType}/{terminalId}/{apiKey}")
    public RestResult register(@PathVariable String apiKey,
                               @PathVariable String terminalType,
                               @PathVariable String terminalId) {
        return grampusClient.register(apiKey, terminalType, terminalId);
    }

    @DeleteMapping("/{terminalType}/{terminalId}/{apiKey}")
    public RestResult unregister(@PathVariable String apiKey,
                                 @PathVariable String terminalType,
                                 @PathVariable String terminalId) {
        return grampusClient.unRegister(apiKey, terminalType, terminalId);
    }
}
