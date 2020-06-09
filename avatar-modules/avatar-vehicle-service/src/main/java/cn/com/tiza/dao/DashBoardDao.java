package cn.com.tiza.dao;

import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.service.dto.DashBoardQuery;
import cn.com.tiza.web.rest.vm.DashBoardVM;
import cn.com.tiza.web.rest.vm.MapPolyInfo;
import cn.com.tiza.web.rest.vm.VehicleMonitorVM;
import org.apache.poi.xssf.model.MapInfo;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DashBoardDao extends BaseMapper<VehicleRealtime> {

    /**
     * 分页查询首页列表信息
     * @param pageQuery 查询条件
     */
    void getDashBoardList(PageQuery<VehicleMonitorVM> pageQuery);

    /**
     * 分页查询首页地图信息
     * @param map 查询条件
     */
    List<MapPolyInfo> getMapInfo(Map<String,Object> map);

    /**
     * 按照省份对车辆进行分类
     * @param map 查询条件
     * @return 分类列表
     */
    List<DashBoardVM> getCountByProvince(Map<String,Object> map);

    /**
     * 按照省市对车辆进行分类
     * @param map 查询条件
     * @return 分类列表
     */
    List<DashBoardVM> getCountByCity(Map<String,Object> map);

    /**
     * 根据vin码获取车辆信息
     * @param vin vin码
     * @return 车辆信息
     */
    VehicleMonitorVM getVehicleInfoByVin(@Param("vin") String vin);

}
