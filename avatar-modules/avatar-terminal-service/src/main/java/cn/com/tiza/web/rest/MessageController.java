package cn.com.tiza.web.rest;

import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.MessageService;
import cn.com.tiza.service.dto.MessageQuery;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.vm.MessageVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * message query
 *
 * @author villas
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService service;

    @GetMapping
    public ResponseEntity<List<MessageVM>> query(MessageQuery query) {
        return ResponseEntity.ok(service.query(query));
    }

    /**
     * 文件导出
     */
    @GetMapping("/export")
    public ResponseEntity<Void> export(HttpServletRequest request, HttpServletResponse response,
                                                MessageQuery query) {
        List<MessageVM> data = service.exportData(query);

        String[] headers = {"终端编号", "SIM卡号", "机器序列号", "协议类型", "报文类型", "数据采集时间",
                "报文长度", "原始报文"};
        String[] columns = {"terminalCode", "sim", "vin", "protocolType", "cmdType", "collectTime",
                "bodyLength", "body"};
        try {
            ExcelWriter.exportExcel(request, response, "报文查询", headers, columns, data);
        } catch (IOException e) {
            log.error("message export exception, the msg is {}", e.getMessage());
            throw new BadRequestAlertException("message export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }

}
