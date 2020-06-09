package cn.com.tiza.dao;

import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.DTCEffectVm;
import cn.com.tiza.web.rest.vm.DashboardAlarmCountVM;
import cn.com.tiza.web.rest.vm.FenceAlarmVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-23
 *
 * @author 0920
 */
public interface AlarmHistoryDao extends BaseMapper<AlarmHistory> {
    /**
     * 报警 分页
     *
     * @param pageQuery
     */
    void pageQueryAlarm(PageQuery<AlarmHistoryVM> pageQuery);

    /**
     * 单车 报警列表
     *
     * @param pageQuery
     */
    void pageQuerySingleVehicleAlarm(PageQuery<AlarmHistoryVM> pageQuery);

    /**
     * 故障 分页
     *
     * @param pageQuery
     */
    void pageQueryFault(PageQuery<AlarmHistoryVM> pageQuery);

    /**
     * 单车 故障列表 支持tla
     *
     * @param pageQuery
     */
    void pageQuerySingleVehicleFault(PageQuery<AlarmHistoryVM> pageQuery);

    /**
     * 单车 故障列表 不支持tla
     *
     * @param pageQuery
     */
    void pageQuerySingleVehicleFault1(PageQuery<AlarmHistoryVM> pageQuery);

    /**
     * app获取故障报警列表
     * @param pageQuery
     */
    void pageQueryAlarmFaultList(PageQuery<AlarmHistoryVM> pageQuery);

    Long getRootOrgIdBySelectId(@Param("organizationId") Long organizationId);

    /**
     * 围栏报警 分页
     *
     * @param pageQuery
     */
    void pageQueryFence(PageQuery<FenceAlarmVM> pageQuery);

    /**
     * 围栏报警 单车 分页
     *
     * @param pageQuery
     */
    void pageQuerySingleVehicleFence(PageQuery<FenceAlarmVM> pageQuery);

    /**
     * 首页报警数量
     *
     * @param organizationId
     */
    List<DashboardAlarmCountVM> count(@Param("organizationId") Long organizationId);

    /**
     * 获取未解除报警状态数量
     * @param organizationId
     * @param financeId
     * @param beginTime
     * @param endTime
     * @param code
     * @return
     */
    Integer countUnDealAlarmOrFault(@Param("organizationId") Long organizationId,
                                    @Param("financeId") Long financeId,
                                    @Param("beginTime") Long beginTime,
                                    @Param("endTime") Long endTime,
                                    @Param("code") String code);
    /**
     * 获取未保养数量
     *
     * @return int
     */
    Integer getMaintenanceCount();

    /**
     * 删除
     *
     * @param vin
     */
    default void deleteByVin(String vin) {
        this.createLambdaQuery().andEq(AlarmHistory::getVin, vin)
                .delete();
    }

    /**
     * 查询车辆故障详情
     * @param id
     * @return
     */
    AlarmHistoryVM getVehicleFaultDetail(@Param("id") Long id);

    /**
     * 查询车辆报警详情
     * @param id
     * @return
     */
    AlarmHistoryVM getVehicleAlarmDetail(@Param("id") Long id);

    /**
     * DTC影响统计
     *
     * @param pageQuery pageQuery
     */
    void dTCEffectStatistic(PageQuery<DTCEffectVm> pageQuery);
}
