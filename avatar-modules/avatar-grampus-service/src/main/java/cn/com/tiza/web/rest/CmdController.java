package cn.com.tiza.web.rest;

import cn.com.tiza.dto.CmdCheckResult;
import cn.com.tiza.dto.CmdSendResult;
import cn.com.tiza.dto.TerminalCmd;
import cn.com.tiza.grampus.GrampusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 指令下发
 *
 * @author tiza
 */
@RestController
@RequestMapping("cmd")
public class CmdController {

    @Autowired
    private GrampusClient grampusClient;

    @PostMapping("/{apiKey}")
    public CmdSendResult cmdSend(@PathVariable String apiKey,
                                 @RequestBody TerminalCmd cmd) {
        return grampusClient.cmdSend(cmd, apiKey);
    }

    @GetMapping("/cmd_response/{checkId}/{apiKey}")
    public CmdCheckResult checkResult(@PathVariable String checkId, @PathVariable String apiKey){
        return grampusClient.checkResult(checkId, apiKey);
    }

}
