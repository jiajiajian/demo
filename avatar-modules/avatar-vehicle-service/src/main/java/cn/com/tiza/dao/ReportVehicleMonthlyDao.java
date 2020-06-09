package cn.com.tiza.dao;

import cn.com.tiza.domain.ReportVehicleMonthly;
import cn.com.tiza.service.dto.MonthAvgWorkTimeQuery;
import cn.com.tiza.service.dto.VehicleModelWorkTimeDto;
import cn.com.tiza.service.dto.VehicleModelWorkTimeQuery;
import cn.com.tiza.web.rest.vm.ActiveVehicleDistributeVM;
import cn.com.tiza.web.rest.vm.MonthAvgWorkTimeVM;
import cn.com.tiza.web.rest.vm.OrgVehicleCountVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-05-07
 */
public interface ReportVehicleMonthlyDao extends BaseMapper<ReportVehicleMonthly> {
    /**
     * 月平均工时
     *
     * @param query
     * @return
     */
    List<MonthAvgWorkTimeVM> monthAvgWorkTime(MonthAvgWorkTimeQuery query);

    /**
     * 活跃车辆分布
     *
     * @param month
     * @param organizationId
     * @return
     */
    List<ActiveVehicleDistributeVM> activeVehicleDistribute(@Param("month") Integer month,
                                                            @Param("organizationId") Long organizationId);


    /**
     * 型号工时统计
     *
     * @return
     */
    List<VehicleModelWorkTimeDto> vehicleModelAvgWorkTime(VehicleModelWorkTimeQuery query);

    /**
     * 代理商工时统计
     * @param query
     * @return
     */
    List<VehicleModelWorkTimeDto> orgAvgWorkTime(VehicleModelWorkTimeQuery query);

    /**
     * 代理商车辆数统计
     * @return
     */
    List<OrgVehicleCountVM> orgVehicleCount();
}
