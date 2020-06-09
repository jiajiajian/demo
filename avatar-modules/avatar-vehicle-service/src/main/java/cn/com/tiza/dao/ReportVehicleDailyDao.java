package cn.com.tiza.dao;

import cn.com.tiza.domain.ReportVehicleDaily;
import cn.com.tiza.service.dto.ReportVehicleDailyDto;
import cn.com.tiza.web.rest.vm.ReportVehicleDailyVM;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.ListUtil;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author villas
 * gen by beetlsql mapper 2020-05-11
 */
public interface ReportVehicleDailyDao extends BaseMapper<ReportVehicleDaily> {

    /**
     * 分析指定车辆统计数据-----油位
     *
     * @param vin      vin
     * @param dailyDto dailyDto
     * @return list
     */
    default List<ReportVehicleDaily> getData(@Param("vin") String vin, ReportVehicleDailyDto dailyDto) {
        List<ReportVehicleDaily> list = createLambdaQuery()
                .andEq(ReportVehicleDaily::getVin, vin)
                .andBetween(ReportVehicleDaily::getCreateTime, dailyDto.getStartTime(), dailyDto.getEndTime())
                .select();

        if (ListUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return list;
    }


    /**
     *  单车分析页面
     * @param pageQuery 查询条件
     * @return 列表
     */
    void getReportDailyData(PageQuery<ReportVehicleDailyVM> pageQuery);

    /**
     * 发动机转速时长分布
     * @param vin 机器序列号
     * @param dailyDto 查询条件
     * @return 列表
     */
    List<ReportVehicleDaily> getEngineData(@Param("vin") String vin, ReportVehicleDailyDto dailyDto);
}
