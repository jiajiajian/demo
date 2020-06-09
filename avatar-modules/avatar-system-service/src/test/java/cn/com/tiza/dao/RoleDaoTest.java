//package cn.com.tiza.dao;
//
//import cn.com.tiza.DbTestConfig;
//import cn.com.tiza.domain.Role;
//import cn.com.tiza.service.dto.RoleQuery;
//import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig;
//import com.ibeetl.starter.BeetlSqlSingleConfig;
//import org.beetl.sql.core.engine.PageQuery;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringJUnitConfig(classes = DbTestConfig.class)
//@TestPropertySource("classpath:beetlsql.properties")
//@Import({BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class})
//public class RoleDaoTest {
//
//    @Autowired
//    private RoleDao roleDao;
//
//    @Test
//    public void pageQuery() {
//        RoleQuery query = new RoleQuery();
//        query.setOrganizationId(1L);
//        PageQuery<Role> pageQuery = query.toPageQuery();
//        roleDao.pageQuery(pageQuery);
//        assertEquals(1, pageQuery.getList().size());
//    }
//
//    @Test
//    public void dropDownListInfo() {
//    }
//}