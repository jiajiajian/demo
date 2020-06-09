package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.VehicleType;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.VehicleTypeService;
import cn.com.tiza.service.dto.VehicleTypeDto;
import cn.com.tiza.service.dto.VehicleTypeQuery;
import cn.com.tiza.service.mapper.VehicleTypeMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleTypeVM;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/vehicleType")

public class VehicleTypeController extends ExcelController {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;

    @GetMapping
    public ResponseEntity<List<VehicleTypeVM>> list(VehicleTypeQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getUser().getRootOrgId());
        }
        PageQuery pageQuery = vehicleTypeService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleTypeVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleTypeService.get(id)
                .map(obj -> vehicleTypeMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid VehicleTypeDto dto) {
        VehicleType newObj = vehicleTypeService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleTypeDto dto) {
        Optional<VehicleType> updatedObj = vehicleTypeService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleTypeService.delete(ids);
        return ResponseEntity.ok().build();
    }

    private String[] titles = {"类型名称", "所属机构", "操作人", "操作时间"};

    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, VehicleTypeQuery query) {
        query.setLimit(Integer.MAX_VALUE - 1);
        query.setPage(1);
        ResponseEntity<List<VehicleTypeVM>> list = this.list(query);
        List<VehicleTypeVM> body = list.getBody();
        download("车辆类型", titles, body, VehicleTypeVM::toRow, request, response);
    }

    /**
     * @param rootOrgId 一级机构
     * @return
     */
    @GetMapping("vehicleTypeOptions/{rootOrgId}")
    public ResponseEntity<List<OptionVM>> vehicleTypeOptions(@PathVariable Long rootOrgId) {
        return ResponseEntity.ok(vehicleTypeService.vehicleTypeOptionsByOrg(rootOrgId));
    }

    /**
     * 查询当前用户类型下拉选
     *
     * @return
     */
    @GetMapping("vehicleTypeOptionsByCurrentUser")
    public ResponseEntity<List<OptionVM>> vehicleTypeOptionsByCurrentUser() {
        return ResponseEntity.ok(vehicleTypeService.vehicleTypeOptionsByCurrentUser());
    }

    /**
     * 根据所选机构查询该机构根组织下所有车辆类型
     *
     * @param orgId 所选机构
     */
    @GetMapping("/optionsBySelectOrg/{orgId}")
    public List<OptionVM> optionsBySelectOrg(@PathVariable Long orgId) {
        return vehicleTypeService.optionsBySelectOrg(orgId);
    }

    /**
     * 根据融资机构获取下拉选
     */
    @GetMapping("/optionsByFinanceId")
    public List<OptionVM> optionsByFinanceId() {
        return vehicleTypeService.optionsByFinanceId(BaseContextHandler.getFinanceId());
    }
}
