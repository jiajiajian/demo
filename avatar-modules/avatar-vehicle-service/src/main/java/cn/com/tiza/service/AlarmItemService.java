package cn.com.tiza.service;


import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.AlarmItem;
import cn.com.tiza.service.dto.AlarmItemDto;
import cn.com.tiza.service.dto.AlarmItemQuery;
import cn.com.tiza.service.mapper.AlarmItemMapper;
import cn.com.tiza.web.rest.vm.AlarmItemVM;
import cn.com.tiza.dao.AlarmItemDao;

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
@Transactional
public class AlarmItemService {

    @Autowired
    private AlarmItemDao alarmItemDao;

    @Autowired
    private AlarmItemMapper alarmItemMapper;

    public PageQuery<AlarmItemVM> findAll(AlarmItemQuery query) {
        PageQuery<AlarmItemVM> pageQuery = query.toPageQuery();
        alarmItemDao.pageQuery(pageQuery);
        List<AlarmItemVM> list = pageQuery.getList();
        //将code转换为name 数据从数据字典获取
        Map<String, String> itemMap = alarmItemDao.findDictItem("ALARM_ITEM").stream()
                .collect(Collectors.toMap(e -> e.get("itemCode"), e -> e.get("itemName"), (k1, k2) -> k2));
        list.forEach(vm -> {
            if(org.springframework.util.StringUtils.hasText(vm.getAlarmItem())){
                List<String> itemList = Arrays.asList(vm.getAlarmItem().split(","));
                String alarmItem = itemList.stream().map(item -> itemMap.get(item)).collect(Collectors.joining(","));
                vm.setAlarmItem(alarmItem);
            }
        });
        return pageQuery;
    }

    public Optional<AlarmItem> get(Long id) {
        return Optional.ofNullable(alarmItemDao.single(id));
    }

    public AlarmItem create(AlarmItemDto command) {
        //检查是否已经创建配置
        AlarmItem itemByOrgId = alarmItemDao.findByOrgId(command.getOrganizationId());
        if (Objects.nonNull(itemByOrgId)) {
            throw new BadRequestException(ErrorConstants.ALARM_ITEM_HAS_CREATED_FOR_THIS_ORGANIZATION);
        }
        AlarmItem entity = alarmItemMapper.dtoToEntity(command);
        alarmItemDao.insert(entity);
        return entity;
    }

    public Optional<AlarmItem> update(Long id, AlarmItemDto command) {
        return get(id).map(entity -> {
            entity.setAlarmItem(StringUtils.join(command.getItems(), ","));
            alarmItemDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        alarmItemDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
