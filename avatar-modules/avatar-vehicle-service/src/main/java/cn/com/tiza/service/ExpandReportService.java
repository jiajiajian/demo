package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.ReportVehicleMonthlyDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.service.dto.AvgModelOilConsumptionQuery;
import cn.com.tiza.service.dto.AvgOilConsumptionQuery;
import cn.com.tiza.service.dto.MonthAvgWorkTimeQuery;
import cn.com.tiza.service.dto.TonnageMonthVehicleNumQuery;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.vm.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tz0920
 */
@Slf4j
@Service
public class ExpandReportService {
    @Autowired
    private ReportVehicleMonthlyDao vehicleMonthlyDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private RedisQueryService redisQueryService;

    public List<MonthWorkTimeChartVM> monthAvgWorkTime(MonthAvgWorkTimeQuery query) {
        List<MonthWorkTimeChartVM> result = Lists.newArrayList();
        if (query.getMergeFlag() == 0) {
            for (int i = 0; i < query.getOrgList().size(); i++) {
                query.setOrganizationId(query.getOrgList().get(i));
                calculateChartData(query, result);
            }
        } else {
            calculateChartData(query, result);
        }
        return result;
    }

    private void calculateChartData(MonthAvgWorkTimeQuery query, List<MonthWorkTimeChartVM> result) {
        MonthWorkTimeChartVM chart = new MonthWorkTimeChartVM();
        List<MonthAvgWorkTimeVM> monthAvgWorkTimeList = vehicleMonthlyDao.monthAvgWorkTime(query);
        /*if (monthAvgWorkTimeList.isEmpty()) {
            result.add(chart);
            return;
        }*/
        Map<String, MonthAvgWorkTimeVM> collect = monthAvgWorkTimeList.stream()
                .collect(Collectors.toMap(MonthAvgWorkTimeVM::getMonth, v -> v));
        //将开始月和结束月之间月份 放进一个list
        ArrayList<String> monthList = Lists.newArrayList();
        int begin = query.getBeginMon();
        StringBuilder builder = new StringBuilder();
        while (begin <= query.getEndMon()) {
            builder.append(String.valueOf(begin).substring(0, 4));
            builder.append("-");
            builder.append(String.valueOf(begin).substring(4, 6));
            monthList.add(builder.toString());
            builder.delete(0, 7);
            String yyyyMMdd = LocalDateTimeUtils.addMonth(1, "yyyyMMdd", String.valueOf(begin + "01"));
            begin = Integer.parseInt(yyyyMMdd.substring(0, 6));
        }

        chart.setXData(monthList);
        List<BigDecimal> yData = new ArrayList<>(monthList.size());
        monthList.forEach(month -> {
            MonthAvgWorkTimeVM avgWorkTime = collect.get(month);
            if (Objects.isNull(avgWorkTime)) {
                yData.add(BigDecimal.ZERO);
            } else {
                BigDecimal divide = BigDecimal.valueOf(avgWorkTime.getWorkTime()).divide(BigDecimal.valueOf(3600 * 1000), 2, BigDecimal.ROUND_HALF_UP);
                yData.add(divide);
            }
        });
        chart.setYData(yData);
        result.add(chart);
    }

    public List<ActiveNumDistributeVM> activeVehicleDistribute(Integer month) {
        List<ActiveVehicleDistributeVM> distributeList = vehicleMonthlyDao.activeVehicleDistribute(month, BaseContextHandler.getOrgId());
        Map<String, List<ActiveVehicleDistributeVM>> collect = distributeList.stream().filter(v -> Objects.nonNull(v.getProvince()))
                .collect(Collectors.groupingBy(ActiveVehicleDistributeVM::getProvince));
        List<ActiveNumDistributeVM> result = new ArrayList<>(collect.size());
        if (distributeList.isEmpty()) {
            return result;
        }
        collect.forEach((k, v) -> {
            ActiveNumDistributeVM vm = new ActiveNumDistributeVM();
            vm.setProvince(k);
            v.forEach(distribute -> {
                Integer tonnage = distribute.getTonnage();
                int count = distribute.getCount();
                if (tonnage == 10) {
                    vm.setT10(count);
                }
                if (tonnage == 20) {
                    vm.setT20(count);
                }
                if (tonnage == 30) {
                    vm.setT30(count);
                }
                if (tonnage == 40) {
                    vm.setT40(count);
                }
            });
            result.add(vm);
        });
        return result;
    }

    public List<AvgOilConsumptionVM> avgOilConsumption(AvgOilConsumptionQuery query) {
        List<AvgOilConsumptionVM> list = vehicleDao.avgOilConsumption(query);
        list.forEach(vm -> {
            Map<String, String> ciData = redisQueryService.ciData(vm.getVin());
            if (Objects.isNull(ciData)) {
                return;
            }
            String avgOil = ciData.get(vm.getCode());
            vm.setVehicleAvgOilConsume(Double.parseDouble(avgOil));
        });
        List<AvgOilConsumptionVM> collect = list.stream()
                .filter(vm -> Objects.nonNull(vm.getVehicleAvgOilConsume()))
                .sorted(Comparator.comparingDouble(AvgOilConsumptionVM::getVehicleAvgOilConsume))
                .collect(Collectors.toList());
        double avg = collect.stream().collect(Collectors.averagingDouble(AvgOilConsumptionVM::getVehicleAvgOilConsume));
        for (int i = 0; i < collect.size(); i++) {
            AvgOilConsumptionVM vm = collect.get(i);
            vm.setModelAvgOilConsume(avg);
            vm.setPercent(BigDecimal.valueOf(i).divide(BigDecimal.valueOf(collect.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
        return collect;
    }

    public List<AvgModelOilConsumptionVM> avgModelOilConsumption(AvgModelOilConsumptionQuery query) {
        List<AvgModelOilConsumptionVM> list = vehicleDao.avgModelOilConsumption(query);
        list.forEach(vm -> {
            Map<String, String> ciData = redisQueryService.ciData(vm.getVin());
            if (Objects.isNull(ciData)
                    || Objects.isNull(vm.getCode())
                    || Objects.isNull(ciData.get(vm.getCode()))) {
               vm.setModelAvgOilConsume(0d);
            }else{
                vm.setModelAvgOilConsume(Double.parseDouble(ciData.get(vm.getCode())));
            }
        });
        Map<String, List<AvgModelOilConsumptionVM>> collect = list.stream().collect(Collectors.groupingBy(AvgModelOilConsumptionVM::getVehicleModel));
        List<AvgModelOilConsumptionVM> result = new ArrayList<>();
        collect.forEach((k,v)->{
            double avg = v.stream().collect(Collectors.averagingDouble(AvgModelOilConsumptionVM::getModelAvgOilConsume));
            AvgModelOilConsumptionVM vm = new AvgModelOilConsumptionVM();
            vm.setVehicleModel(k);
            vm.setModelAvgOilConsume(avg);
            vm.setNum(v.size());
            for(int i=0;i<v.size();i++){
                if(Objects.nonNull(v.get(i).getModelAvgOilConsume())){
                    if(i == 0){
                        vm.setModelMaxOilConsume(v.get(i).getModelAvgOilConsume());
                        vm.setModelMinOilConsume(v.get(i).getModelAvgOilConsume());
                    }
                    if(this.compareDouble(vm.getModelMinOilConsume(),v.get(i).getModelAvgOilConsume()) == 1){
                        vm.setModelMinOilConsume(v.get(i).getModelMinOilConsume());
                    }
                    if(this.compareDouble(vm.getModelMaxOilConsume(),v.get(i).getModelAvgOilConsume()) == -1){
                        vm.setModelMaxOilConsume(v.get(i).getModelAvgOilConsume());
                    }
                }
            }
            result.add(vm);
        });
        return result;
    }


    public List<TonnageMonthVehicleNumVM> monthTonnageVehicleNum(TonnageMonthVehicleNumQuery query) {
        List<TonnageMonthGroupVM> list = vehicleDao.monthTonnageVehicleNum(query);
        Map<String,List<TonnageMonthGroupVM>> tonnageGroup = list.stream().collect(Collectors.groupingBy(TonnageMonthGroupVM::getMonth));
        List<String> monthList = Lists.newArrayList();
        int begin = query.getBeginMon();
        StringBuilder builder = new StringBuilder();
        while (begin <= query.getEndMon()) {
            builder.append(String.valueOf(begin), 0, 4);
            builder.append(String.valueOf(begin), 4, 6);
            monthList.add(builder.toString());
            builder.delete(0, builder.length());
            String yyyyMMdd = LocalDateTimeUtils.addMonth(1, "yyyyMMdd", String.valueOf(begin + "01"));
            begin = Integer.parseInt(yyyyMMdd.substring(0, 6));
        }
        List<TonnageMonthVehicleNumVM> result = Lists.newArrayList();
        for (int i=0;i<monthList.size();i++){
            String item = monthList.get(i);
            TonnageMonthVehicleNumVM vm = new TonnageMonthVehicleNumVM();
            vm.setMonth(item);
            vm.setTonnage(query.getTonnage());
            vm.setLess30(0);vm.setLess30Per(0);
            vm.setBetween240To500(0);vm.setBetween240To500Per(0);
            vm.setBetween30To240(0);vm.setBetween30To240Per(0);
            vm.setMore500(0);vm.setMore500Per(0);
            if(Objects.nonNull(tonnageGroup.get(item))) {
                int total = 0;
                for(int j=0;j<tonnageGroup.get(item).size();j++){
                    Integer num = tonnageGroup.get(item).get(j).getNum();
                    String type = tonnageGroup.get(item).get(j).getType();
                    switch (type){
                        case "less30":
                            vm.setLess30(num);break;
                        case "30to240":
                            vm.setBetween30To240(num);break;
                        case "240to500":
                            vm.setBetween240To500(num);break;
                        case "more500":
                            vm.setMore500(num);break;
                    }
                  total += num;
                }
                vm.setTotalNum(total);
            }
            if(Objects.nonNull(vm.getTotalNum()) && vm.getTotalNum() >0){
                vm.setLess30Per(new Double(Math.floor(100*vm.getLess30()/vm.getTotalNum())).intValue());
                vm.setBetween30To240Per(new Double(Math.floor(100*vm.getBetween30To240()/vm.getTotalNum())).intValue());
                vm.setBetween240To500Per(new Double(Math.floor(100*vm.getBetween240To500()/vm.getTotalNum())).intValue());
                vm.setMore500Per(new Double(Math.floor(100*vm.getMore500()/vm.getTotalNum())).intValue());
            }

            result.add(vm);
        }
        return result;
    }

    private int compareDouble(Double a,Double b){
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        return data1.compareTo(data2);
    }

}
