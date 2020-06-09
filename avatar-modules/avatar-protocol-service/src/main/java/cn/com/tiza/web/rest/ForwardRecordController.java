package cn.com.tiza.web.rest;

import cn.com.tiza.domain.TaskData;
import cn.com.tiza.dto.RestDataRecord;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.ForwardRecordService;
import cn.com.tiza.service.PlatformService;
import cn.com.tiza.service.dto.ForwardRecordDto;
import cn.com.tiza.service.dto.TaskQuery;
import cn.com.tiza.service.dto.VehicleQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author villas
 * @since 2019/5/30 13:33
 */
@Slf4j
@RestController
@RequestMapping("forwardRecord")
public class ForwardRecordController {

    @Autowired
    private ForwardRecordService forwardRecordService;

    @Autowired
    private PlatformService platformService;

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity list(TaskQuery query) {
        PageQuery<TaskData> pageQuery = forwardRecordService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{taskId}/vehicles")
    @SuppressWarnings("unchecked")
    public ResponseEntity queryVehicles(@PathVariable Long taskId, VehicleQuery query) {
        PageQuery pageQuery = forwardRecordService.queryVehicles(taskId, query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/notRelated/{taskId}/vehicles")
    @SuppressWarnings("unchecked")
    public ResponseEntity queryNotRelatedVehicles(@PathVariable Long taskId, VehicleQuery query) {
        PageQuery pageQuery = forwardRecordService.queryNotRelatedVehicles(taskId, query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }


    @GetMapping("failed/{taskId}/{vin}")
    public ResponseEntity<List<RestDataRecord>> queryRangeData(@PathVariable Long taskId, @PathVariable String vin,
                                                               @Valid ForwardRecordDto dto) {
        return ResponseEntity.ok(platformService.queryPartialData(vin, dto.getStartTime(),
                dto.getEndTime(), dto.getCmdID(), taskId));
    }

    @GetMapping("export/failed/{taskId}/{vin}")
    public ResponseEntity<Void> exportReport(@PathVariable Long taskId, @PathVariable String vin, ForwardRecordDto dto,
                                             HttpServletRequest request, HttpServletResponse response) {
        List<RestDataRecord> content = platformService.queryRangeData(vin, dto.getStartTime(), dto.getEndTime(),
                dto.getCmdID(), taskId);
        String[] headers = {"失败类型", "数据时间", "接收时间", "原始报文"};
        String[] columns = {"cmdId", "time", "rt", "body"};
        String fileName = vin + "_fail_report";
        try {
            ExcelWriter.exportExcel(request, response, fileName, headers, columns, content,
                    record -> {
                        HashMap<String, String> map = Maps.newHashMap();
                        map.put("cmdId", this.convertFailType(record.getCmdID()));
                        map.put("time", ExcelWriter.timeConvert(record.getTime()));
                        map.put("rt", ExcelWriter.timeConvert(record.getRt()));
                        map.put("body", record.getBody());
                        return map;
                    });
        } catch (Exception e) {
            log.error("export exception is {}", e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    private String convertFailType(int cmd) {
        if (cmd == FailType.FAIL.value) {
            return FailType.FAIL.description;
        } else if (cmd == FailType.TIMEOUT.value) {
            return FailType.TIMEOUT.description;
        } else {
            return "";
        }
    }

    @Getter
    enum FailType {
        /**
         * 应答失败
         */
        FAIL(218, "应答失败"),
        /**
         * 补发超时
         */
        TIMEOUT(219, "补发超时");
        private Integer value;
        private String description;

        FailType(Integer value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
