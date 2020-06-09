package cn.com.tiza.web.rest;

import cn.com.tiza.domain.TerminalTest;
import cn.com.tiza.dto.CommandDto;
import cn.com.tiza.service.CmdService;
import cn.com.tiza.service.dto.AccParam;
import cn.com.tiza.service.dto.CmdQuery;
import cn.com.tiza.service.dto.TerminalParam;
import cn.com.tiza.util.CmdConstant;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import com.vip.vjtools.vjkit.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 指令交互
 *
 * @author villas
 */
@Slf4j
@RestController
@RequestMapping("/cmd")
public class CmdController {

    @Autowired
    private CmdService cmdService;

    @PutMapping("/location/{terminal}")
    public ResponseEntity location(@PathVariable String terminal) {
        cmdService.location(terminal, CmdConstant.LOCATION);
        return ResponseEntity.ok().build();
    }

    /**
     * 工况查询
     *
     * @param terminal 终端编号
     */
    @PutMapping("/working/{terminal}")
    public ResponseEntity working(@PathVariable String terminal) {
        cmdService.working(terminal, CmdConstant.TOTAL_DATA);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upgrade/{terminal}")
    public ResponseEntity upgrade(@PathVariable String terminal, @RequestBody Map<String, String> urlMap) {
        String url = urlMap.get("url");
        if (!StringUtils.hasText(url)) {
            throw new BadRequestException("upgrade.url.is.null");
        }
        cmdService.upgrade(terminal, CmdConstant.UPGRADE, url);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trace/{terminal}/{time}")
    public ResponseEntity trace(@PathVariable String terminal, @PathVariable int time) {
        cmdService.trace(terminal, CmdConstant.TRACE, time);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/release/alarm/{terminal}")
    public ResponseEntity releaseAlarm(@PathVariable String terminal) {
        cmdService.releaseAlarm(terminal, CmdConstant.RELEASE_ALARM);
        return ResponseEntity.ok().build();
    }

    /**
     * acc开关巡检
     *
     * @param terminal terminal
     */
    @PutMapping("/acc/{terminal}")
    public ResponseEntity accParam(@PathVariable String terminal, @RequestBody AccParam acc) {
        cmdService.accParam(terminal, CmdConstant.WORK_PARAM_SET, acc);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/param/setting/{terminal}")
    public ResponseEntity generalParamSetting(@PathVariable String terminal, @RequestBody List<TerminalParam> params) {
        if (ListUtil.isEmpty(params)) {
            throw new BadRequestAlertException("set param is null", null, "set.param.is.null");
        }
        cmdService.generalParamSetting(terminal, params);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/param/setting/{terminal}")
    public ResponseEntity generalParamQuery(@PathVariable String terminal, @RequestBody List<TerminalParam> params) {
        if (ListUtil.isEmpty(params)) {
            throw new BadRequestAlertException("set param is null", null, "set.param.is.null");
        }
        cmdService.generalParamQuery(terminal, params);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/lock/{itemKey}")
    public ResponseEntity<Map<String, Object>> lock(@PathVariable String itemKey, @RequestBody CmdQuery cmdQuery) {
        cmdService.lock(cmdQuery);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{terminalCode}/tests")
    public ResponseEntity<List<TerminalTest>> terminalTests(@PathVariable String terminalCode) {
        return ResponseEntity.ok(cmdService.terminalTests(terminalCode));
    }

    @PostMapping("/command/add")
    public ResponseEntity<Long> saveCommand(@RequestBody CommandDto dto){
        Long commandId = cmdService.save(dto);
        return ResponseEntity.ok(commandId);
    }

}
