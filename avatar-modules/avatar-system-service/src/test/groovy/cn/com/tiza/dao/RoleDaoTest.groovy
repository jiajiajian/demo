package cn.com.tiza.dao

import cn.com.tiza.DbTestConfig
import cn.com.tiza.service.dto.RoleQuery
import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig
import com.ibeetl.starter.BeetlSqlSingleConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import spock.lang.Specification

@SpringJUnitConfig(classes = DbTestConfig.class)
@TestPropertySource("classpath:beetlsql.properties")
@Import([BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class])
class RoleDaoTest extends Specification {

    @Autowired
    RoleDao roleDao;

    def "test pageQuery"() {
        given:
        def query = new RoleQuery();
        query.organizationId = (1L);
        def pageQuery = query.toPageQuery();

        when:
        roleDao.pageQuery(pageQuery);
        then:
        pageQuery.totalRow == 1
    }

}
