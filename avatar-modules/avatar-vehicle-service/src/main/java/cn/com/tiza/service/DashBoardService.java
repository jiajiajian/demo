package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.DashBoardDao;
import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.service.dto.DashBoardQuery;
import cn.com.tiza.service.dto.VehicleRealtimeQuery;
import cn.com.tiza.web.rest.vm.DashBoardVM;
import cn.com.tiza.web.rest.vm.MapPolyInfo;
import cn.com.tiza.web.rest.vm.VehicleMonitorVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DashBoardService {
    @Autowired
    private DashBoardDao dashBoardDao;
    @Autowired
    private VehicleRealtimeService vehicleRealtimeService;

    /**
     * 分页查询首页列表信息
     *
     * @param query 查询条件
     */
    public PageQuery<VehicleMonitorVM> getDashBoardList(DashBoardQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        PageQuery<VehicleMonitorVM> pageQuery = query.toPageQuery();
        dashBoardDao.getDashBoardList(pageQuery);
        List<VehicleMonitorVM> vehicleDebugVMList = pageQuery.getList().stream().map(this::setTerminalStatus).collect(Collectors.toList());
        pageQuery.setList(vehicleDebugVMList);
        return pageQuery;
    }

    /**
     * 分页查询首页地图信息
     *
     * @param query 查询条件
     */
    public List<MapPolyInfo> getMapInfo(DashBoardQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        Map<String, Object> map = query.params();
        List<MapPolyInfo> list = dashBoardDao.getMapInfo(map).stream().map(this::setOnlineStatus).collect(Collectors.toList());
        return list;
    }

    /**
     * 按照省市对车辆进行分类
     *
     * @param query 查询条件
     * @return 分类列表
     */
    public List<DashBoardVM> getCountByProvince(DashBoardQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        Map<String, Object> map = query.params();
        List<DashBoardVM> provinceList = dashBoardDao.getCountByProvince(map);
        List<DashBoardVM> cityList = dashBoardDao.getCountByCity(map);
        for (DashBoardVM dashBoardVM : provinceList) {
            if (Objects.isNull(dashBoardVM.getProvince())) {
                dashBoardVM.setProvince("未知");
            }
            dashBoardVM.setSubShow(false);
            List<DashBoardVM> list = new ArrayList<>();
            for (DashBoardVM temp : cityList) {
                if ((!Objects.isNull(temp.getProvince())) && temp.getProvince().equals(dashBoardVM.getProvince())) {
                    list.add(temp);
                }
            }
            dashBoardVM.setList(list);
        }
        return provinceList;
    }

    /**
     * 获取在线离线和总车辆数
     */
    public Map<String, Object> getCountByType() {
        HashMap<String, Object> map = new HashMap<>(16);
        VehicleRealtimeQuery vehicleRealtimeQuery=new VehicleRealtimeQuery();
        if (Objects.isNull(vehicleRealtimeQuery.getOrganizationId())) {
            vehicleRealtimeQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        vehicleRealtimeQuery.setFinanceId(BaseContextHandler.getFinanceId());
        vehicleRealtimeQuery.setFinanceId(BaseContextHandler.getFinanceId());
        vehicleRealtimeQuery.setLimit(Integer.MAX_VALUE);
        vehicleRealtimeQuery.setPage(1);
        PageQuery<VehicleRealtime> pageQuery = vehicleRealtimeService.findAll(vehicleRealtimeQuery);
        List<VehicleRealtime> list = pageQuery.getList();
        Integer totalCount = list.size();
        long offlineCount = list.stream().filter(temp -> Objects.isNull(temp.getDataUpdateTime()) || temp.getDataUpdateTime() < System.currentTimeMillis() - 300000).count();
        long onlineCount = totalCount - offlineCount;
        map.put("totalCount", totalCount);
        map.put("onlineCount", onlineCount);
        map.put("offlineCount", offlineCount);
        return map;
    }

    private MapPolyInfo setOnlineStatus(MapPolyInfo mapPolyInfo) {
        if (null != mapPolyInfo.getDataUpdateTime() && mapPolyInfo.getDataUpdateTime() + 300000 > System.currentTimeMillis()) {
            mapPolyInfo.setOnlineStatus(1);
        } else {
            mapPolyInfo.setOnlineStatus(0);
        }
        return mapPolyInfo;
    }
    private VehicleMonitorVM setTerminalStatus(VehicleMonitorVM vehicleMonitorVM) {
        if (null != vehicleMonitorVM.getDataUpdateTime() && vehicleMonitorVM.getDataUpdateTime() + 300000 > System.currentTimeMillis()) {
            vehicleMonitorVM.setTerminalStatus(1);
        } else {
            vehicleMonitorVM.setTerminalStatus(0);
        }
        return vehicleMonitorVM;
    }
    /**
     * 根据vin码获取车辆信息
     *
     * @param vin vin码
     * @return 车辆信息
     */
    public VehicleMonitorVM getVehicleInfoByVin(String vin) {
        return dashBoardDao.getVehicleInfoByVin(vin);
    }
}