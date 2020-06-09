package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.TlaDao;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.Tla;
import cn.com.tiza.service.dto.AlarmHistoryQuery;
import cn.com.tiza.service.dto.FenceAlarmQuery;
import cn.com.tiza.web.rest.dto.FunctionSetItemVM;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.DashboardAlarmCountVM;
import cn.com.tiza.web.rest.vm.FenceAlarmVM;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Slf4j
@Service
public class AlarmHistoryService {

    @Autowired
    private AlarmHistoryDao alarmHistoryDao;

    @Autowired
    private TlaDao tlaDao;


    public PageQuery<AlarmHistoryVM> pageQueryAlarm(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQueryAlarm(pageQuery);
        return pageQuery;
    }

    public PageQuery<AlarmHistoryVM> pageQuerySingleVehicleAlarm(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQuerySingleVehicleAlarm(pageQuery);
        return pageQuery;
    }

    public PageQuery<AlarmHistoryVM> pageQueryAlarmFaultList(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQueryAlarmFaultList(pageQuery);
        return pageQuery;
    }

    public PageQuery<AlarmHistoryVM> pageQueryFault(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQueryFault(pageQuery);
        return pageQuery;
    }

    public PageQuery<AlarmHistoryVM> pageQuerySingleVehicleFault(AlarmHistoryQuery query) {
        return queryVehicleFault(query);
    }

    private PageQuery<AlarmHistoryVM> queryVehicleFault(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        int size = tlaDao.createLambdaQuery()
                .andEq(Tla::getOrganizationId, query.getRootOrg())
                .select().size();
        if (size == 0) {
            alarmHistoryDao.pageQuerySingleVehicleFault1(pageQuery);
        } else {
            alarmHistoryDao.pageQuerySingleVehicleFault(pageQuery);
        }
        return pageQuery;
    }

    public PageQuery<Map> pageQuerySingleVehicleHistoryFault(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = queryVehicleFault(query);
        PageQuery<Map> pageQuery1 = new PageQuery<>();
        if (query.getIsHistory()) {
            List<AlarmHistoryVM> list = pageQuery.getList();

            List<HashMap<String, Object>> resultList = list.stream().map(vm -> {
                HashMap<String, Object> map = Maps.newHashMap();
                copyBeanToMap(vm, map);
                map.put("recentlyCondition", null);
                //拼装功能集数据
                if (StringUtils.hasText(vm.getRecentlyCondition())) {
                    List<FunctionSetItemVM> funList = JSON.parseArray(vm.getRecentlyCondition(), FunctionSetItemVM.class);
                    Map<String, String> collect = funList.stream()
                            .filter(i -> Objects.nonNull(i.getCode()))
                            .collect(Collectors.toMap(FunctionSetItemVM::getCode, FunctionSetItemVM::getValue));
                    map.putAll(collect);
                }
                return map;
            }).collect(Collectors.toList());
            pageQuery1.setTotalRow(pageQuery.getTotalRow());
            pageQuery1.setList(resultList);
        }

        return pageQuery1;
    }

    private void copyBeanToMap(AlarmHistoryVM vm, HashMap<String, Object> map) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(vm.getClass());
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
            if (properties != null && properties.length > 0) {
                for (PropertyDescriptor prop : properties) {
                    //2.得到属性名
                    String name = prop.getName();
                    //3.过滤class属性
                    if (!"class".equals(name)) {
                        //4.得到属性的get方法
                        Method getter = prop.getReadMethod();

                        //5.获取属性值
                        Object value = getter.invoke(vm);
                        //6.放入map中
                        if (value != null) {
                            map.put(name, value);
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public PageQuery<FenceAlarmVM> pageQueryFence(FenceAlarmQuery query) {
        PageQuery<FenceAlarmVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQueryFence(pageQuery);
        return pageQuery;
    }

    public PageQuery<FenceAlarmVM> pageQuerySingleVehicleFence(FenceAlarmQuery query) {
        PageQuery<FenceAlarmVM> pageQuery = query.toPageQuery();
        alarmHistoryDao.pageQuerySingleVehicleFence(pageQuery);
        return pageQuery;
    }

    public Optional<AlarmHistory> get(Long id) {
        return Optional.ofNullable(alarmHistoryDao.single(id));
    }

    public Map<String, Integer> count() {
        Map<String, Integer> map = alarmHistoryDao.count(BaseContextHandler.getOrgId())
                .stream()
                .collect(Collectors.toMap(k -> k.getAlarmType().getName(), DashboardAlarmCountVM::getCount));
        Integer maintenanceCount = alarmHistoryDao.getMaintenanceCount();
        map.put("MAINTENANCE", maintenanceCount);
        return map;
    }

    public Integer countUnDealAlarmOrFault(AlarmHistoryQuery query) {
        return alarmHistoryDao.countUnDealAlarmOrFault(query.getOrganizationId(), query.getFinanceId(), query.getBeginTime(), query.getEndTime(), query.getCode());
    }

    public AlarmHistoryVM getVehicleAlarmDetail(Long id) {
        return alarmHistoryDao.getVehicleAlarmDetail(id);
    }

    public AlarmHistoryVM getVehicleFaultDetail(Long id) {
        return alarmHistoryDao.getVehicleFaultDetail(id);
    }


}
