package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.MaintenanceInfoDao;
import cn.com.tiza.dao.UserDao;
import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.dto.AlarmMessage;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.dto.Message;
import cn.com.tiza.web.rest.WebSocketClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.vm.MaintenanceContentVM;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;
import cn.com.tiza.web.rest.vm.VehicleInfoVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.MaintenanceLog;
import cn.com.tiza.service.dto.MaintenanceLogDto;
import cn.com.tiza.service.dto.MaintenanceLogQuery;
import cn.com.tiza.service.mapper.MaintenanceLogMapper;
import cn.com.tiza.web.rest.vm.MaintenanceLogVM;
import cn.com.tiza.dao.MaintenanceLogDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MaintenanceLogService {

    @Autowired
    private MaintenanceLogDao maintenanceLogDao;

    @Autowired
    private MaintenanceInfoDao maintenanceInfoDao;

    @Autowired
    private MaintenanceLogMapper maintenanceLogMapper;

    public PageQuery<MaintenanceLogVM> findAll(MaintenanceLogQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        maintenanceLogDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<MaintenanceLog> get(Long id) {
        return Optional.ofNullable(maintenanceLogDao.single(id));
    }

    public Optional<MaintenanceLogVM> getLogDetail(Long id) {
        MaintenanceLogVM maintenanceLogVM = new MaintenanceLogVM();
        List<String> contents = new ArrayList<>();
        MaintenanceLog maintenanceLog = maintenanceLogDao.single(id);
        maintenanceLogVM.setHandleResult(maintenanceLog.getHandleResult());
        List<MaintenanceInfoVM> infoList = maintenanceInfoDao.getInfoListByLogId(id);
        for (MaintenanceInfoVM vm : infoList) {
            String content = vm.getMaintenanceContent();
            contents.add(content);
        }
        maintenanceLogVM.setContent(contents);
        return Optional.ofNullable(maintenanceLogVM);
    }

    public MaintenanceLog create(MaintenanceLogDto command) {
        MaintenanceLog entity = maintenanceLogMapper.dtoToEntity(command);
        maintenanceLogDao.insert(entity);
        return entity;
    }

    public Optional<MaintenanceLog> update(Long id, MaintenanceLogDto command) {
        return get(id).map(entity -> {
            entity.setHandleStatus(1);
            entity.setHandleResult(command.getHandleResult());
            entity.setHandleTime(System.currentTimeMillis());
            entity.setHandleUserAccount(BaseContextHandler.getLoginName());
            entity.setHandleUserRealname(BaseContextHandler.getName());
            return entity;
        });
    }

    public void delete(Long id) {
        maintenanceLogDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public Map<String, Integer> count() {
        Integer count = maintenanceLogDao.count();
        HashMap map = new HashMap(4);
        map.put("MAINTENANCE", count);
        return map;
    }

    public void produceMaintenanceLog() {
        List<VehicleInfoVM> vehicleInfoVMList = maintenanceLogDao.getVehicleInfo();
        for (VehicleInfoVM vehicleInfoVM : vehicleInfoVMList) {
            List<MaintenanceContentVM> contentVMList = maintenanceLogDao.getMaintenanceInfo(vehicleInfoVM.getVehicleTypeId(), vehicleInfoVM.getVehicleModelId());
            List<List<MaintenanceContentVM>> groupList = new ArrayList<>();
            contentVMList.stream().collect(Collectors.groupingBy(e -> e.getMaintenanceType().toString() + e.getHours().toString(), Collectors.toList()))
                    .forEach((key, value) -> {
                        groupList.add(value);
                    });
            for (List<MaintenanceContentVM> list : groupList) {
                StringBuilder stringBuilder = new StringBuilder();
                for (MaintenanceContentVM maintenanceContentVM : list) {
                    if (!Objects.isNull(maintenanceContentVM) && maintenanceContentVM.getMaintenanceType() == 0) {
                        if (vehicleInfoVM.getTotalWorkTime() > maintenanceContentVM.getHours() - 24 && vehicleInfoVM.getTotalWorkTime() < maintenanceContentVM.getHours()) {
                            stringBuilder.append(maintenanceContentVM.getMaintenanceContent()).append(",");
                        }
                    }else {
                        List<MaintenanceInfo> maintenanceInfoList=maintenanceLogDao.getMaintenanceInfoByInfo(maintenanceContentVM.getTacticsId(),0,maintenanceContentVM.getMaintenanceContent());
                        if (maintenanceInfoList.size()>1){
                            throw new BadRequestException("maintenanceInfo.data.error");
                        }else if (maintenanceInfoList.size()<1){
                            if (vehicleInfoVM.getTotalWorkTime()%maintenanceContentVM.getHours() > maintenanceContentVM.getHours() - 24 && vehicleInfoVM.getTotalWorkTime()%maintenanceContentVM.getHours() < maintenanceContentVM.getHours()) {
                                stringBuilder.append(maintenanceContentVM.getMaintenanceContent()).append(",");
                            }
                        }else {
                            double totalWorkTime=vehicleInfoVM.getTotalWorkTime()-maintenanceContentVM.getHours();
                            if (totalWorkTime%maintenanceContentVM.getHours() > maintenanceContentVM.getHours() - 24 && totalWorkTime%maintenanceContentVM.getHours() < maintenanceContentVM.getHours()) {
                                stringBuilder.append(maintenanceContentVM.getMaintenanceContent()).append(",");
                            }
                        }
                    }
                }
                if (stringBuilder.length()>1){
                    stringBuilder = stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
                    List<MaintenanceLog> logList=maintenanceLogDao.getLogList(vehicleInfoVM.getVin(),list.get(0).getMaintenanceType(),stringBuilder.toString());
                    if (logList.size()>1){
                        MaintenanceLog maintenanceLog=logList.get(0);
                        if((int)(maintenanceLog.getRemindHours()/list.get(0).getHours())==(int)(vehicleInfoVM.getTotalWorkTime()/list.get(0).getHours())){
                            continue;
                        }
                    }
                    MaintenanceLog maintenanceLog=new MaintenanceLog();
                    maintenanceLog.setVin(vehicleInfoVM.getVin());
                    maintenanceLog.setMaintenanceType(list.get(0).getMaintenanceType());
                    maintenanceLog.setMaintenanceContent(stringBuilder.toString());
                    maintenanceLog.setIntervalHours(list.get(0).getHours());
                    maintenanceLog.setRemindHours(vehicleInfoVM.getTotalWorkTime());
                    maintenanceLog.setRemindTime(System.currentTimeMillis());
                    maintenanceLog.setHandleStatus(0);
                    maintenanceLogDao.insert(maintenanceLog);
                }
            }
        }
    }


}
