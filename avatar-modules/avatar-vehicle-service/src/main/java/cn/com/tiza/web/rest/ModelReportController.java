package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.ModelReportService;
import cn.com.tiza.service.dto.VehicleModelWorkTimeQuery;
import cn.com.tiza.web.rest.vm.WorkTimeReportVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/modelReport")
public class ModelReportController extends ExcelController {

    @Autowired
    private ModelReportService modelReportService;

    /**
     * 型号工时统计
     *
     * @param query
     * @return
     */
    @GetMapping("/workTime")
    public ResponseEntity<List<WorkTimeReportVM>> monthWorkTime(@Valid VehicleModelWorkTimeQuery query) {
        return ResponseEntity.ok(modelReportService.getWorkTimeExportList(modelReportService.monthAvgWorkTime(query),query));
    }

    /**
     * 型号工时统计导出
     * @param request
     * @param response
     * @param query
     * @return
     */
    @GetMapping("/work/time/export")
    public void exportWorkTime(HttpServletRequest request, HttpServletResponse response,
                                               VehicleModelWorkTimeQuery query) {
        List<WorkTimeReportVM> modelWorkTimeList  = modelReportService.getWorkTimeExportList(modelReportService.monthAvgWorkTime(query),query);
        String[] title = {"型号","Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","合计(H)"};
        download("型号工时统计", title, modelWorkTimeList, WorkTimeReportVM::toRow, request, response);
    }
}
