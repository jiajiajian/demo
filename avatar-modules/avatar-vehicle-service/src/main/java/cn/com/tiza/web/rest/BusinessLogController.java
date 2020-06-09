package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.BusinessLog;
import cn.com.tiza.service.BusinessLogService;
import cn.com.tiza.service.dto.BusinessLogQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.BusinessLogVM;
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
import java.util.Objects;

/**
 * Controller
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/businessLog")
public class BusinessLogController {

    @Autowired
    private BusinessLogService businessLogService;


    @GetMapping
    public ResponseEntity<List<BusinessLogVM>> list(BusinessLogQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = businessLogService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/history/{vin}/{operateType}")
    public ResponseEntity<List<BusinessLogVM>> history(@PathVariable("vin") String vin, @PathVariable("operateType") Integer operateType) {
        return ResponseEntity.ok(businessLogService.history(vin, operateType));
    }
}
