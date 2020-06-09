package cn.com.tiza.service;

import cn.com.tiza.dal.beetlsql.AbstractTestClass;
import cn.com.tiza.dal.beetlsql.SqlKit;
import cn.com.tiza.dao.SimcardDao;
import cn.com.tiza.service.dto.SimcardQuery;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.jupiter.api.Test;

public class SimcardServiceTest extends AbstractTestClass {

    SimcardDao dao = SqlKit.mapper(SimcardDao.class);

    @Test
    void findAll() {
        SimcardQuery query = new SimcardQuery();
        PageQuery pageQuery = query.toPageQuery();
        dao.pageQuery(pageQuery);
    }
}