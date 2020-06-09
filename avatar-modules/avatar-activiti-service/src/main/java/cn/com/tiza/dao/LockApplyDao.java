package cn.com.tiza.dao;

import cn.com.tiza.domain.LockApply;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author villas
 * gen by beetlsql mapper 2020-04-27
 */
public interface LockApplyDao extends BaseMapper<LockApply> {
    /**
     * 获取申请单号
     *
     * @param pageQuery pageQuery
     */
    void pageQuery(PageQuery<LockApply> pageQuery);

    /**
     * 修改流程实例id
     *
     * @param applyCode  applyCode
     * @param instanceId instanceId
     */
    void updateInstanceId(@Param("applyCode") String applyCode, @Param("instanceId") String instanceId);


    /**
     * 根据businesskey 获取申请人
     * @param applyCode
     * @return
     */
    LockApply getApplyByCode(@Param("applyCode") String applyCode);

    void updateStateByCode(@Param("applyCode") String applyCode,@Param("state") int state);

    void updateStateByInstanceId(@Param("instanceId") String instanceId,@Param("state") int state);
}
