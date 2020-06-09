package cn.com.tiza.dao;

import cn.com.tiza.domain.Tla;
import cn.com.tiza.web.rest.vm.TlaVM;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-05-12
 */
public interface TlaDao extends BaseMapper<Tla> {

    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<TlaVM> pageQuery);

    default Tla findByNameAndOrg(String name,Long rootOrgId){
        return createLambdaQuery()
                .andEq(Tla::getTla,name)
                .andEq(Tla::getOrganizationId,rootOrgId)
                .single();
    }

    default Tla findByTlaIdAndOrg(String tlaId,Long rootOrgId){
        return createLambdaQuery()
                .andEq(Tla::getTlaId,tlaId)
                .andEq(Tla::getOrganizationId,rootOrgId)
                .single();
    }

}
