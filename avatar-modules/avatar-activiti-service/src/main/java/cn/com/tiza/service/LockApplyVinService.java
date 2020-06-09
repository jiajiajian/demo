package cn.com.tiza.service;


import cn.com.tiza.dao.LockApplyVinDao;
import cn.com.tiza.domain.LockApplyVin;
import cn.com.tiza.service.dto.LockApplyQuery;
import cn.com.tiza.web.rest.vm.LockApplyVehicleVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class LockApplyVinService {

    @Autowired
    private LockApplyVinDao lockApplyVinDao;

    @SuppressWarnings("unchecked")
    public PageQuery<LockApplyVehicleVM> pageQuery(LockApplyQuery query){
        PageQuery pageQuery = query.toPageQuery();
        lockApplyVinDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public void save(Long applyId, String vin) {
        LockApplyVin applyVin = new LockApplyVin(applyId, vin);
        lockApplyVinDao.insert(applyVin);
    }

    public void saveBatch(List<String> vinList, Long applyId) {
        List<LockApplyVin> applyVins = vinList.stream()
                .map(vin -> new LockApplyVin(applyId, vin))
                .collect(Collectors.toList());
        lockApplyVinDao.insertBatch(applyVins);
    }

    public void delete(Long applyId) {
        lockApplyVinDao.createLambdaQuery().andEq(LockApplyVin::getApplyId, applyId).delete();
    }

}
