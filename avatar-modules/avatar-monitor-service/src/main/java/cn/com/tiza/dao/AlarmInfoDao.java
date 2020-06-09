package cn.com.tiza.dao;

import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.web.rest.vm.AlarmInfoVM;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-03-23
 */
public interface AlarmInfoDao extends BaseMapper<AlarmInfo> {
    void pageQuery(PageQuery pageQuery);

    /**
     * 根据vin 报警项 查询
     *
     * @param vin
     * @param alarmCode
     * @return
     */
    default AlarmInfo findByAlarmCodeAndVin(String vin, String alarmCode) {
        return createLambdaQuery()
                .andEq(AlarmInfo::getVin, vin)
                .andEq(AlarmInfo::getAlarmCode, alarmCode)
                .desc(AlarmInfo::getId)
                .single();
    }

    /**
     * 根据vin spn fmi 查询故障数据
     *
     * @param vin
     * @param spnFmi
     * @return
     */
    default AlarmInfo findBySpnFmiAndVin(String spnFmi, String vin) {
        return createLambdaQuery()
                .andEq(AlarmInfo::getVin, vin)
                .andEq(AlarmInfo::getSpnFmi, spnFmi)
                .desc(AlarmInfo::getId)
                .single();
    }

    /**
     * 根据vin fenceId 查询故障数据
     * @param vin
     * @param fenceId
     * @return
     */
    default AlarmInfo findByFenceIdAndVin(Long fenceId, String vin) {
        return createLambdaQuery()
                .andEq(AlarmInfo::getVin, vin)
                .andEq(AlarmInfo::getFenceId, fenceId)
                .desc(AlarmInfo::getId)
                .single();
    }

    default void deleteByVin(String vin) {
        this.createLambdaQuery().andEq(AlarmInfo::getVin, vin)
                .delete();
    }

    /**
     * 单车 报警列表
     *
     * @param pageQuery
     */
    void pageQueryCurrentVehicleFault(PageQuery<AlarmInfoVM> pageQuery);

}
