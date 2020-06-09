package cn.com.tiza.dao;

import cn.com.tiza.DbTestConfig;
import cn.com.tiza.dal.beetlsql.Config;
import cn.com.tiza.dal.beetlsql.SqlKit;
import cn.com.tiza.domain.Organization;
import cn.com.tiza.web.rest.vm.OrganizationVM;
import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig;
import com.ibeetl.starter.BeetlSqlSingleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig(classes = DbTestConfig.class)
@TestPropertySource("classpath:beetlsql.properties")
@Import({BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class})
public class OrganizationDaoTest {

    @Autowired
    private OrganizationDao dao;

    @Test
    public void getOrgChild() {
        List<OrganizationVM> orgs = dao.getOrgChild(1L);
        //orgs.(this::printlnJson);
    }

    public static void main(String[] args) {
        Config.dbInit("mysql");
        OrganizationDao dao = SqlKit.mapper(OrganizationDao.class);
        Organization root = new Organization();
        root.setId(-1L);
        dao.insert(root);
//        List<Organization> all = dao.all();
//        all.forEach(org -> {
//            if (!org.hasParent()) {
//                org.setPath(org.genPath(null));
//                dao.updateById(org);
//            } else {
//                Organization parent = dao.single(org.getParentOrgId());
//                org.setPath(org.genPath(parent));
//                dao.updateById(org);
//            }
//        });
    }
}