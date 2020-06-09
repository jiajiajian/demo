package cn.com.tiza.service;


import cn.com.tiza.dao.BusinessLogDao;
import cn.com.tiza.domain.BusinessLog;
import cn.com.tiza.service.dto.BusinessLogQuery;
import cn.com.tiza.web.rest.vm.BusinessLogVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Slf4j
@Service
public class BusinessLogService {

    @Autowired
    private BusinessLogDao businessLogDao;


    public PageQuery<BusinessLogVM> findAll(BusinessLogQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        businessLogDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public List<BusinessLogVM> history(String vin, Integer operateType) {
        return businessLogDao.history(vin,operateType);
    }

}
