package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.MaintenanceBindDao;
import cn.com.tiza.dao.MaintenanceInfoDao;
import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.MaintenanceTactics;
import cn.com.tiza.service.dto.MaintenanceTacticsDto;
import cn.com.tiza.service.dto.MaintenanceTacticsQuery;
import cn.com.tiza.service.mapper.MaintenanceTacticsMapper;
import cn.com.tiza.web.rest.vm.MaintenanceTacticsVM;
import cn.com.tiza.dao.MaintenanceTacticsDao;

import java.util.List;
import java.util.Objects;
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
public class MaintenanceTacticsService {

    @Autowired
    private MaintenanceTacticsDao maintenanceTacticsDao;

    @Autowired
    private MaintenanceTacticsMapper maintenanceTacticsMapper;

    @Autowired
    private MaintenanceBindDao maintenanceBindDao;

    @Autowired
    private MaintenanceInfoDao maintenanceInfoDao;

    public PageQuery<MaintenanceTactics> findAll(MaintenanceTacticsQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<MaintenanceTactics> pageQuery = query.toPageQuery();
        maintenanceTacticsDao.pageQuery(pageQuery);
        for(MaintenanceTactics maintenanceTactics:pageQuery.getList()){
            List<MaintenanceBind> bindList = maintenanceTacticsDao.getBindListByTacticsId(maintenanceTactics.getId());
            StringBuffer bindNames=new StringBuffer();
            for (MaintenanceBind maintenanceBind:bindList){
                bindNames.append(maintenanceBind.getModelName()).append(",");
            }
            if (bindNames.length()<1){
                maintenanceTactics.setBindNames(bindNames.toString());
            }else {
                maintenanceTactics.setBindNames( bindNames.substring(0,bindNames.length()-1));
            }

        }        return pageQuery;
    }

    public Optional<MaintenanceTactics> get(Long id) {
        return Optional.ofNullable(maintenanceTacticsDao.single(id));
    }

    public Optional<MaintenanceTacticsVM> getDetail(Long id) {
        MaintenanceTactics maintenanceTactics = maintenanceTacticsDao.single(id);
        List<MaintenanceBind> bindList = maintenanceTacticsDao.getBindListByTacticsId(id);
        List<MaintenanceInfoVM> infoList = maintenanceInfoDao.getInfoListByTacticsId(id);
        MaintenanceTacticsVM maintenanceTacticsVM = maintenanceTacticsMapper.toVM(maintenanceTactics);
        maintenanceTacticsVM.setBindList(bindList);
        maintenanceTacticsVM.setInfoList(infoList);
        return Optional.ofNullable(maintenanceTacticsVM);
    }

    public MaintenanceTactics create(MaintenanceTacticsDto command) {
        List<MaintenanceBind> list = command.getBindList();
        MaintenanceTactics entity = maintenanceTacticsMapper.dtoToEntity(command);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setOrganizationId(BaseContextHandler.getRootOrgId());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        maintenanceTacticsDao.insert(entity,true);
        for (MaintenanceBind item : list) {
            item.setMaintenanceTacticsId(entity.getId());
            item.setCreateTime(System.currentTimeMillis());
            item.setCreateUserAccount(BaseContextHandler.getLoginName());
            item.setCreateUserRealname(BaseContextHandler.getName());
        }
        maintenanceBindDao.insertBatch(list);
        return entity;
    }

    public Optional<MaintenanceTactics> update(Long id, MaintenanceTacticsDto command) {
        List<MaintenanceBind> bindList = maintenanceTacticsDao.getBindListByTacticsId(id);
        for (MaintenanceBind item : bindList) {
            maintenanceBindDao.deleteById(item.getId());
        }
        List<MaintenanceBind> list = command.getBindList();
        for (MaintenanceBind item : list) {
            item.setMaintenanceTacticsId(id);
            item.setCreateTime(System.currentTimeMillis());
            item.setCreateUserAccount(BaseContextHandler.getLoginName());
            item.setCreateUserRealname(BaseContextHandler.getName());
        }
        maintenanceBindDao.insertBatch(list);
        return get(id).map(entity -> {
            entity.setId(command.getId());
            entity.setRemark(command.getRemark());
            entity.setTacticsName(command.getTacticsName());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            maintenanceTacticsDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        maintenanceTacticsDao.deleteById(id);
        List<MaintenanceBind> bindList = maintenanceTacticsDao.getBindListByTacticsId(id);
        for (MaintenanceBind item : bindList) {
            maintenanceBindDao.deleteById(item.getId());
        }
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
            List<MaintenanceBind> bindList = maintenanceTacticsDao.getBindListByTacticsId(id);
            for (MaintenanceBind item : bindList) {
                maintenanceBindDao.deleteById(item.getId());
            }
        }
    }
}
