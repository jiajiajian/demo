package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.web.rest.vm.MaintenanceDetailVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.service.dto.MaintenanceInfoDto;
import cn.com.tiza.service.dto.MaintenanceInfoQuery;
import cn.com.tiza.service.mapper.MaintenanceInfoMapper;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;
import cn.com.tiza.dao.MaintenanceInfoDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class MaintenanceInfoService {

    @Autowired
    private MaintenanceInfoDao maintenanceInfoDao;

    @Autowired
    private MaintenanceInfoMapper maintenanceInfoMapper;

    public List<MaintenanceDetailVM> getInfoListByTacticsId(Long tacticsId) {
        List<MaintenanceInfoVM> list= maintenanceInfoDao.getInfoListByTacticsId(tacticsId);
        List<List<MaintenanceInfoVM>> groupList = new ArrayList<>();
        List<MaintenanceDetailVM> detailVMList=new ArrayList<>();
        list.stream()
                .collect(Collectors.groupingBy(e->e.getMaintenanceType().toString()+e.getHours().toString(),Collectors.toList()))
                .forEach((key,value)->{
                    groupList.add(value);
                });
        for (List<MaintenanceInfoVM> item:groupList){
            MaintenanceDetailVM maintenanceDetailVM=new MaintenanceDetailVM();
            maintenanceDetailVM.setType(item.get(0).getMaintenanceType());
            maintenanceDetailVM.setHours(item.get(0).getHours());
            maintenanceDetailVM.setItems(item);
            detailVMList.add(maintenanceDetailVM);
        }
        return detailVMList;
    }

    public Optional<MaintenanceInfo> get(Long id) {
        return Optional.ofNullable(maintenanceInfoDao.single(id));
    }

    public List<MaintenanceInfo> create(MaintenanceInfoDto command) {
        List<String> contents = command.getContents();
        List<MaintenanceInfo> maintenanceInfoList = new ArrayList<>();
        for (String content : contents) {
            MaintenanceInfo maintenanceInfo = new MaintenanceInfo();
            maintenanceInfo.setHours(command.getHours());
            maintenanceInfo.setTacticsId(command.getTacricsId());
            maintenanceInfo.setMaintenanceType(command.getMaintenanceType());
            maintenanceInfo.setCreateTime(System.currentTimeMillis());
            maintenanceInfo.setCreateUserAccount(BaseContextHandler.getLoginName());
            maintenanceInfo.setCreateUserRealname(BaseContextHandler.getName());
            maintenanceInfo.setMaintenanceContent(content);
            maintenanceInfoList.add(maintenanceInfo);
        }
        maintenanceInfoDao.insertBatch(maintenanceInfoList);
        return maintenanceInfoList;
    }

    public Optional<MaintenanceInfo> update(Long id, MaintenanceInfoDto command) {
        return get(id).map(entity -> {
            entity.setId(command.getId());
            entity.setHours(command.getHours());
            entity.setMaintenanceType(command.getMaintenanceType());
            entity.setCreateTime(command.getCreateTime());
            entity.setCreateUserAccount(command.getCreateUserAccount());
            entity.setCreateUserRealname(command.getCreateUserRealname());
            entity.setMaintenanceContent(command.getMaintenanceContent());
            entity.setRemark(command.getRemark());
            entity.setTacticsId(command.getTacricsId());
            return entity;
        });
    }

    public void delete(Long id) {
        maintenanceInfoDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
