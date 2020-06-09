package cn.com.tiza.dao;

import cn.com.tiza.domain.FaultDict;
import org.beetl.sql.core.mapper.BaseMapper;

import javax.validation.constraints.NotNull;

/**
 * @author fanha
 */
public interface FaultDictDao extends BaseMapper<FaultDict> {

    default long countByOrgId(@NotNull Long organizationId){
        return createLambdaQuery()
                .andEq(FaultDict::getOrganizationId,organizationId)
                .count();
    }
}
