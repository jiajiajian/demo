package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.DataAnalyseDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.dao.VehicleMonitorDao;
import cn.com.tiza.service.dto.DataAnalyseQuery;
import cn.com.tiza.web.rest.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service
 * gen by beetlsql 2020-04-21
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DataAnalyseService {
    @Autowired
    private DataAnalyseDao dataAnalyseDao;
    @Autowired
    private VehicleDao vehicleDao;

    /**
     * 区域车辆统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public List<VehicleAnalyseVM> getCountByProvince(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (Objects.isNull(dataAnalyseQuery)||Objects.isNull(dataAnalyseQuery.getProvince())) {
           return dataAnalyseDao.getCountByProvince(dataAnalyseQuery);
        }else {
            return dataAnalyseDao.getCountByCity(dataAnalyseQuery);
        }
    }

    /**
     * 机型统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public List<VehicleAnalyseVM> getCountByVehicleType(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        return dataAnalyseDao.getCountByVehicleType(dataAnalyseQuery);
    }

    /**
     * 总工作小时统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public List<VehicleAnalyseVM> getTotalWorkTimeByProvince(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (Objects.isNull(dataAnalyseQuery)||Objects.isNull(dataAnalyseQuery.getProvince())) {
            return dataAnalyseDao.getTotalWorkTimeByProvince(dataAnalyseQuery);
        }else {
            return dataAnalyseDao.getTotalWorkTimeByCity(dataAnalyseQuery);
        }
    }

    /**
     * 工作时间统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public List<WorkTimeVM> getWorkTime(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        return dataAnalyseDao.getWorkTime(dataAnalyseQuery);
    }

    /**
     * 工作时间明细
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public WorkTimeData getWorkTimeData(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (!Objects.isNull(dataAnalyseQuery.getEndTime())){
            dataAnalyseQuery.setEndTime(dataAnalyseQuery.getEndTime()+1000*60*60*24);
        }
        WorkTimeData workTimeData=new WorkTimeData();
        List<WorkTimeDetailVM> workTimeDetailVMList = dataAnalyseDao.getWorkTimeDetail(dataAnalyseQuery);
        workTimeData.setWorkTimeDetailVMList(workTimeDetailVMList);
        List<MachineWorkDetailVM> machineWorkDetailVMList=dataAnalyseDao.getMachineWorkDetail(dataAnalyseQuery);
        workTimeData.setMachineWorkDetailVMList(machineWorkDetailVMList);
        return workTimeData;
    }

    /**
     * 在线率统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    public List<OnlineRateVM> getOnlineRate(DataAnalyseQuery dataAnalyseQuery){
        if (Objects.isNull(dataAnalyseQuery.getOrganizationId())){
            dataAnalyseQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        List<OnlineRateVM> list =dataAnalyseDao.getOnlineRate(dataAnalyseQuery);
        long totalCount = vehicleDao.allCount();
        for (OnlineRateVM onlineRateVM:list){
            double rate = ((onlineRateVM.getOnlineCount().doubleValue())/(totalCount))*100;
            onlineRateVM.setOnlineRate(String.format("%.2f", rate)+"%");
            onlineRateVM.setOfflineCount(totalCount-onlineRateVM.getOnlineCount());
        }
        return list;
    }

}
