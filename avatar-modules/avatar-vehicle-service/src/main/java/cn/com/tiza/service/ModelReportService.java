package cn.com.tiza.service;

import cn.com.tiza.dao.ReportVehicleMonthlyDao;
import cn.com.tiza.service.dto.VehicleModelWorkTimeDto;
import cn.com.tiza.service.dto.VehicleModelWorkTimeQuery;
import cn.com.tiza.util.CalculateUtil;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.vm.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class ModelReportService {

    @Autowired
    private ReportVehicleMonthlyDao vehicleMonthlyDao;

    /**
     * 型号工时统计
     * @param query
     * @return
     */
    public  Map<String,Map<Integer,Double>> monthAvgWorkTime(VehicleModelWorkTimeQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<VehicleModelWorkTimeDto> workTimeList = vehicleMonthlyDao.vehicleModelAvgWorkTime(query);
        Map<String, Map<Integer, Double>> result = getWorkTimeDetail(query, workTimeList);
        return result;
    }

    /**
     * 型号工时统计到处
     * @param query
     * @return
     */
    public List<WorkTimeReportVM> getWorkTimeExportList(Map<String,Map<Integer,Double>> modelWorkTimeList,VehicleModelWorkTimeQuery query){
        List<WorkTimeReportVM> result = new ArrayList<>();
        modelWorkTimeList.forEach((k,v)->{
            WorkTimeReportVM vm = new WorkTimeReportVM();
            vm.setName(k);
            vm.setJan(v.get(Integer.parseInt(query.getYear()+"01")));
            vm.setFeb(v.get(Integer.parseInt(query.getYear()+"02")));
            vm.setMar(v.get(Integer.parseInt(query.getYear()+"03")));
            vm.setApr(v.get(Integer.parseInt(query.getYear()+"04")));
            vm.setMay(v.get(Integer.parseInt(query.getYear()+"05")));
            vm.setJun(v.get(Integer.parseInt(query.getYear()+"06")));
            vm.setJul(v.get(Integer.parseInt(query.getYear()+"07")));
            vm.setAug(v.get(Integer.parseInt(query.getYear()+"08")));
            vm.setSep(v.get(Integer.parseInt(query.getYear()+"09")));
            vm.setOct(v.get(Integer.parseInt(query.getYear()+"10")));
            vm.setNov(v.get(Integer.parseInt(query.getYear()+"11")));
            vm.setDec(v.get(Integer.parseInt(query.getYear()+"12")));
            vm.setTotal(v.get(query.getYear()));
            result.add(vm);
        });
        return result;
    }

    /**
     * 代理商车辆数
     * @return
     */
    public  List<OrgVehicleCountVM> getOrgVehicleCount( ) {
        return vehicleMonthlyDao.orgVehicleCount();
    }

    /**
     * 代理商工时统计
     * @param query
     * @return
     */
    public Map<String,Map<Integer,Double>> orgMonthAvgWorkTime(VehicleModelWorkTimeQuery query){
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<VehicleModelWorkTimeDto> workTimeList = vehicleMonthlyDao.orgAvgWorkTime(query);
        Map<String, Map<Integer, Double>> result = getWorkTimeDetail(query, workTimeList);
        return result;
    }

    private ArrayList<Integer> getMonthOfYear(VehicleModelWorkTimeQuery query) {
        int begin = Integer.parseInt(new StringBuilder().append(query.getYear()).append("01").toString());
        int end   = Integer.parseInt(new StringBuilder().append(query.getYear()).append("12").toString());
        ArrayList<Integer> monthList = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        while (begin <= end) {
            builder.append(String.valueOf(begin).substring(0, 4));
            builder.append(String.valueOf(begin).substring(4, 6));
            monthList.add(Integer.parseInt(builder.toString()));
            builder.delete(0, builder.length());
            String yyyyMMdd = LocalDateTimeUtils.addMonth(1, "yyyyMMdd", String.valueOf(begin + "01"));
            begin = Integer.parseInt(yyyyMMdd.substring(0, 6));
        }
        return monthList;
    }

    private Map<String, Map<Integer, Double>> getWorkTimeDetail(VehicleModelWorkTimeQuery query, List<VehicleModelWorkTimeDto> workTimeList) {
        Map<String, Map<Integer, Double>> result = new HashMap<>();
        for (int j = 0; j < workTimeList.size(); j++) {
            Map<Integer, Double> monthWorkTimeMap = result.get(workTimeList.get(j).getName());
            Map<Integer, Double> tmpMap;
            if (Objects.nonNull(monthWorkTimeMap)) {
                tmpMap = result.get(workTimeList.get(j).getName());
            } else {
                tmpMap = new HashMap<>();
            }
            tmpMap.put(workTimeList.get(j).getMonth(), workTimeList.get(j).getWorkTime());
            result.put(workTimeList.get(j).getName(), tmpMap);
        }
        //获取1到12月份
        ArrayList<Integer> monthList = getMonthOfYear(query);
        result.forEach((k, v) -> {
            BigDecimal totalWorkTime = new BigDecimal(Double.toString(0d));
            for (int i = 0; i < monthList.size(); i++) {
                if (Objects.isNull(v.get(monthList.get(i)))) {
                    v.put(monthList.get(i), 0.00d);
                }
                totalWorkTime = CalculateUtil.add(totalWorkTime, new BigDecimal(Double.toString(v.get(monthList.get(i)))));
            }
            v.put(query.getYear(), totalWorkTime.doubleValue());
        });
        return result;
    }
}
