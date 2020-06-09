package cn.com.tiza.dao;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.domain.Organization;
import cn.com.tiza.web.rest.vm.OrganizationVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;
import java.util.Optional;

/**
 * 组织dao
 * @author TZ0855
 */
public interface OrganizationDao extends BaseMapper<Organization> {

    default Optional<Organization> get(Long id) {
        return Optional.ofNullable(single(id));
    }

    /**
     * 查询根级组织列表
     * @return list of organization
     */
    default List<Organization> rootList() {
        return createLambdaQuery()
                .andEq(Organization::getParentOrgId, Constants.ROOT_ORG_ID)
                .select();
    }
    /**
     * 查询所有
     * @return list
     */
    List<OrganizationVM> findAll();

    /**
     * 查询机构所有下级(包括自己)
     * @param orgId id
     * @return list
     */
    List<OrganizationVM> getOrgChild(@Param("orgId") Long orgId);


    List<Organization> getChild(@Param("orgId") Long orgId);

    /**
     * 计算直属下级组织数
     * @param id id
     * @return id.children.count()
     */
    default long countChildren(Long id) {
        return createLambdaQuery()
                .andEq(Organization::getParentOrgId, id)
                .count();
    }

    /**
     * 查询同组织下的机构
     * @param parentId
     * @param name
     * @return
     */
    default Optional<Organization> findByParentIdAndName(Long parentId, String name) {
        return Optional.ofNullable(createLambdaQuery()
                .andEq(Organization::getParentOrgId, parentId)
                .andEq(Organization::getOrgName, name)
                .single());
    }
}
