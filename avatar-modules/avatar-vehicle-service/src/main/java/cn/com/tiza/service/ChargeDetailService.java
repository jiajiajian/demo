package cn.com.tiza.service;


import cn.com.tiza.dao.ChargeDetailDao;
import cn.com.tiza.domain.ChargeDetail;
import cn.com.tiza.service.dto.ChargeDetailDto;
import cn.com.tiza.service.mapper.ChargeDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class ChargeDetailService {

    @Autowired
    private ChargeDetailDao chargeDetailDao;

    @Autowired
    private ChargeDetailMapper chargeDetailMapper;


    public Optional<ChargeDetail> get(Long id) {
        return Optional.ofNullable(chargeDetailDao.single(id));
    }




    public void delete(Long id) {
        chargeDetailDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
