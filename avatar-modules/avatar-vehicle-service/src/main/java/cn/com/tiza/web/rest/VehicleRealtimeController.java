package cn.com.tiza.web.rest;

import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.service.VehicleRealtimeService;
import cn.com.tiza.service.dto.VehicleRealtimeDto;
import cn.com.tiza.service.dto.VehicleRealtimeQuery;
import cn.com.tiza.service.dto.WorkTimeDto;
import cn.com.tiza.service.mapper.VehicleRealtimeMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.VehicleRealtimeVM;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/vehicleRealtime")

public class VehicleRealtimeController {

    @Autowired
    private VehicleRealtimeService vehicleRealtimeService;

    @Autowired
    private VehicleRealtimeMapper vehicleRealtimeMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<VehicleRealtimeVM>> list(VehicleRealtimeQuery query) {
        PageQuery pageQuery = vehicleRealtimeService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(vehicleRealtimeMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<VehicleRealtimeVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleRealtimeService.get(id)
                .map(obj -> vehicleRealtimeMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid VehicleRealtimeDto dto) {
        VehicleRealtime newObj = vehicleRealtimeService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleRealtimeDto dto) {
        Optional<VehicleRealtime> updatedObj = vehicleRealtimeService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleRealtimeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleRealtimeService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("work/time/statistic")
    public ResponseEntity<List<Map<String, Object>>> workTimeStatistic(WorkTimeDto dto) {
        return ResponseEntity.ok(vehicleRealtimeService.workTimeStatistic(dto));
    }

    @GetMapping("work/time/statistic/export")
    public ResponseEntity<List<Map<String, Object>>> workTimeStatisticExport(
            HttpServletRequest request, HttpServletResponse response, WorkTimeDto dto) {
        try {
            vehicleRealtimeService.workTimeStatisticExport(request, response, "终端信息", dto);
        } catch (IOException e) {
            log.error("workTimeStatisticExport export exception, the msg is {}", e.getMessage());
            throw new BadRequestAlertException("terminal export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }
}
