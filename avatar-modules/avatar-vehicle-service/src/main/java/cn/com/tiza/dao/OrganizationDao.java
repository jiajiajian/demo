package cn.com.tiza.dao;

import cn.com.tiza.domain.Organization;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * 组织dao
 *
 * @author
 */
public interface OrganizationDao extends BaseMapper<Organization> {

    /**
     * find
     *
     * @param rootOrgId
     * @param orgName
     * @return Organization
     */
    default Organization findOrgByRootIdAndName(Long rootOrgId, String orgName) {
        return createLambdaQuery()
                .andEq(Organization::getOrgName, orgName)
                .andEq(Organization::getRootOrgId, rootOrgId)
                .single();
    }


    /**
     * find
     *
     * @param orgName
     * @return Organization
     */
    default List<Organization> findOrgByName(String orgName) {
        return createLambdaQuery()
                .andEq(Organization::getOrgName, orgName)
                .select();
    }
}
