package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Fence;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.FenceService;
import cn.com.tiza.service.VehicleLocationService;
import cn.com.tiza.service.dto.FenceDto;
import cn.com.tiza.service.dto.FenceQuery;
import cn.com.tiza.service.dto.FenceVehicleQuery;
import cn.com.tiza.service.mapper.FenceMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.FenceVM;
import cn.com.tiza.web.rest.vm.VehicleVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/fence")
public class FenceController {

    @Autowired
    private FenceService fenceService;

    @Autowired
    private FenceMapper fenceMapper;

    @Autowired
    private VehicleLocationService vehicleLocationService;

    /**
     * 机构 融资用户只可看自己机构下策略 管理员可随意查询
     *
     * @param query
     * @return
     */
    @GetMapping
    public ResponseEntity<List<FenceVM>> list(FenceQuery query) {
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name()) && Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            query.setFinanceId(BaseContextHandler.getFinanceId());
        }
        PageQuery pageQuery = fenceService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FenceVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(fenceService.get(id)
                .map(obj -> fenceMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid FenceDto dto) {
        Fence newObj = fenceService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FenceDto dto) {
        Optional<Fence> updatedObj = fenceService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fenceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        fenceService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 未关联围栏车辆列表
     *
     * @param query
     * @return
     */
    @GetMapping("/unRelatedVehicles/{fenceId}")
    public ResponseEntity<List<VehicleVM>> pageQueryUnRelatedVehicles(@PathVariable Long fenceId, FenceVehicleQuery query) {
        setQueryVehicleParams(fenceId, query);
        PageQuery pageQuery = fenceService.pageQueryUnRelatedVehicles(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * 围栏已关联车辆列表
     *
     * @param query
     * @return
     */
    @GetMapping("/alreadyRelatedVehicles/{fenceId}")
    public ResponseEntity<List<VehicleVM>> pageQueryRelatedVehicles(@PathVariable Long fenceId, FenceVehicleQuery query) {
        setQueryVehicleParams(fenceId, query);
        PageQuery pageQuery = fenceService.pageQueryRelatedVehicles(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    private void setQueryVehicleParams(@PathVariable Long fenceId, FenceVehicleQuery query) {
        query.setFenceId(fenceId);
        //融资机构用户车辆列表 需要根据车辆销售信息的融资机构查询，可能会跨越多个一级机构
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            query.setFinanceId(BaseContextHandler.getFinanceId());
        } else if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name()) && Objects.isNull(query.getOrganizationId())) {
            //若为机构用户
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
    }

    /**
     * 删除该组织的所有围栏关联了此车的数据
     *
     * @param vin
     * @param orgId
     */
    @DeleteMapping("/deleteRelatedVehiclesByOrgId/{vin}/{orgId}")
    public void deleteRelatedVehiclesByOrgId(@PathVariable("vin") String vin, @PathVariable("orgId") Long orgId) {
        fenceService.deleteRelatedVehiclesByOrgId(vin, orgId);
    }

    /**
     * 关联车辆
     *
     * @param fenceId
     * @param vinList
     */
    @PutMapping("/relateVehicles/{fenceId}")
    public void relateVehicles(@PathVariable Long fenceId, @RequestBody List<String> vinList) {
        fenceService.relateVehicles(fenceId, vinList);
    }

    /**
     * 解除关联车辆
     *
     * @param fenceId
     * @param vinList
     */
    @PutMapping("/unRelateVehicles/{fenceId}")
    public void unRelateVehicles(@PathVariable Long fenceId, @RequestBody List<String> vinList) {
        fenceService.unRelateVehicles(fenceId, vinList);
    }

    /**
     * 根据经纬度获取详细地址
     * @param lon 经度
     * @param lat 纬度
     * @return 详细地址
     */
    @GetMapping("/getLocation")
    public ResponseEntity<String> getLocation(String lon, String lat) {
        String address=vehicleLocationService.getLocationByLonAndLat(lon,lat);
        return ResponseEntity.ok(address);
    }

}
