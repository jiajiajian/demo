package cn.com.tiza.service;

import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.dao.BaseDicItemDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.BaseDicItem;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author villas
 */
@Service
public class GrampusApiServiceImp implements GrampusApiService {
    private static final String T_PROTOCOL_TYPE = "T_PROTOCOL_TYPE";

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private BaseDicItemDao dicItemDao;

    @Override
    public String getApiKey(String terminalType) {
        return Optional.ofNullable(dicItemDao.createLambdaQuery()
                .andEq(BaseDicItem::getDicCode, T_PROTOCOL_TYPE)
                .andEq(BaseDicItem::getItemCode, terminalType).single())
                .map(BaseDicItem::getRemark)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_DO_NOT_FIND_API_KEY));
    }

    public String getTerminalType(String vin) {
        return vehicleDao.findProtocolType(vin);
    }
}
