package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.VehicleModel;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.VehicleModelService;
import cn.com.tiza.service.dto.VehicleModelDto;
import cn.com.tiza.service.dto.VehicleModelQuery;
import cn.com.tiza.service.mapper.VehicleModelMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleModelVM;
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
@RequestMapping("/vehicleModel")
public class VehicleModelController extends ExcelController {

    @Autowired
    private VehicleModelService vehicleModelService;

    @Autowired
    private VehicleModelMapper vehicleModelMapper;

    @GetMapping
    public ResponseEntity<List<VehicleModelVM>> list(VehicleModelQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getRootOrgId());
        }
        PageQuery pageQuery = vehicleModelService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleModelVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleModelService.get(id)
                .map(obj -> vehicleModelMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid VehicleModelDto dto) {
        VehicleModel newObj = vehicleModelService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleModelDto dto) {
        Optional<VehicleModel> updatedObj = vehicleModelService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleModelService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleModelService.delete(ids);
        return ResponseEntity.ok().build();
    }

    private String[] titles = {"型号名称", "车辆类型", "所属机构", "操作人", "操作时间"};

    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, VehicleModelQuery query) {
        query.setLimit(Integer.MAX_VALUE - 1);
        query.setPage(1);
        ResponseEntity<List<VehicleModelVM>> list = this.list(query);
        List<VehicleModelVM> body = list.getBody();
        download("车辆型号", titles, body, VehicleModelVM::toRow, request, response);
    }

    /**
     * 根据组织查询车型
     *
     * @param orgId
     * @return
     */
    @GetMapping("/vehicleModelOptionsByOrg/{orgId}")
    public ResponseEntity<List<OptionVM>> vehicleModelOptionsByOrg(@PathVariable("orgId") Long orgId) {
        return ResponseEntity.ok(vehicleModelService.vehicleModelOptionsByOrg(orgId));
    }

    /**
     * 查询当前用户类型下拉选
     * @return
     */
    @GetMapping("vehicleModelOptionsByCurrentUser")
    public ResponseEntity<List<OptionVM>> vehicleModelOptionsByCurrentUser() {
        return ResponseEntity.ok(vehicleModelService.vehicleModelOptionsByCurrentUser());
    }

    /**
     * 根据车辆类型 查询车型
     *
     * @param vehicleTypeId
     * @return
     */
    @GetMapping("/vehicleModelOptionsByVehicleType/{vehicleTypeId}")
    public ResponseEntity<List<OptionVM>> vehicleModelOptionsByVehicleType(@PathVariable("vehicleTypeId") Long vehicleTypeId) {
        return ResponseEntity.ok(vehicleModelService.vehicleModelOptionsVehicleType(vehicleTypeId));
    }


    /**
     * 根据融资机构查询车型
     *
     * @return
     */
    @GetMapping("/optionsByFinanceId")
    public ResponseEntity<List<OptionVM>> optionsByFinanceId() {
        return ResponseEntity.ok(vehicleModelService.optionsByFinanceId(BaseContextHandler.getFinanceId()));
    }

}
