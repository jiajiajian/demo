package cn.com.tiza.dao;

import cn.com.tiza.domain.AlarmItem;
import cn.com.tiza.web.rest.vm.AlarmItemVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface AlarmItemDao extends BaseMapper<AlarmItem> {
    /**
     * 分页查询
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<AlarmItemVM> pageQuery);

    /**
     * 根据组织查询报警项配置
     *
     * @param orgId
     * @return
     */
    default AlarmItem findByOrgId(Long orgId) {
        return createLambdaQuery()
                .andEq(AlarmItem::getOrganizationId, orgId)
                .single();
    }

    /**
     * 根据code查询字典项
     *
     * @param code
     *
     * @return
     */
    List<Map<String, String>> findDictItem(@Param("code") String code);
}
