package cn.com.tiza.dao;

import cn.com.tiza.domain.FaultDict;
import cn.com.tiza.domain.Fence;
import org.beetl.sql.core.mapper.BaseMapper;

import javax.validation.constraints.NotNull;

/**
 * @author fanha
 */
public interface FenceDao extends BaseMapper<Fence> {

    default long countByOrgId(@NotNull Long organizationId){
        return createLambdaQuery()
                .andEq(Fence::getOrganizationId,organizationId)
                .count();
    }
}
