package cn.com.tiza.web.rest;

import cn.com.tiza.cmd.command.domain.Command;
import cn.com.tiza.domain.CmdRes;
import cn.com.tiza.service.CmdLogService;
import cn.com.tiza.service.dto.CmdLogQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.vm.CmdLogVM;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/cmdLog")
public class CmdLogController {

    @Autowired
    private CmdLogService cmdLogService;


    @GetMapping("/{vin}")
    public ResponseEntity<List<CmdLogVM>> list(@PathVariable String vin, CmdLogQuery query) {
        query.setVin(vin);
        PageQuery<Command> pageQuery = cmdLogService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(this.convert(pageQuery.getList()), headers, HttpStatus.OK);
    }

    private List<CmdLogVM> convert(List<Command> commands){
        if(ListUtil.isEmpty(commands)){
            return Lists.newArrayList();
        }
        return commands.stream().map(this::convert).collect(Collectors.toList());
    }

    private CmdLogVM convert(Command command){
        CmdLogVM vm = new CmdLogVM();
        vm.setVin(command.getVin());
        vm.setCmd(command.getCmdId());
        vm.setCmdName(command.getItemName());
        vm.setRunStateName(Optional.ofNullable(command.getState()).map(CmdRes::getStatus).orElse(""));
        vm.setRunTime(command.getOperateTime());
        vm.setLoginName(command.getUsername());
        return vm;
    }
}
