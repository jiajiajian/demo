package cn.com.tiza.dao;

import cn.com.tiza.domain.Vehicle;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author 00
 */
public interface VehicleDao extends BaseMapper<Vehicle> {
    default Vehicle findByVin(String vin) {
        return createLambdaQuery()
                .andEq(Vehicle::getVin, vin)
                .single();
    }
}
