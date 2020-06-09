package cn.com.tiza.dao

import cn.com.tiza.DbTestConfig
import cn.com.tiza.domain.User
import cn.com.tiza.service.dto.UserQuery
import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig
import com.ibeetl.starter.BeetlSqlSingleConfig
import org.beetl.sql.core.BeetlSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import spock.lang.Specification

@SpringJUnitConfig(classes = DbTestConfig.class)
@TestPropertySource("classpath:beetlsql.properties")
@Import([BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class])
class UserDaoTest extends Specification {

    @Autowired
    UserDao userDao;

    def "loginName is unique"() {
        given:
        def user = new User()
        user.loginName = "admin"

        when:
        userDao.insert(user)

        then:
        def ex = thrown(BeetlSQLException)
        ex.message.contains("Unique index or primary key violation")
    }

    def "test pageQuery"() {
        given:
        def query = new UserQuery();
        query.loginName = "ad";
        query.realName = "ad";
        query.organizationId = 1L;
        def pageQuery = query.toPageQuery();

        when:
        userDao.pageQuery(pageQuery)

        then:
        pageQuery.getTotalRow() == 1
        pageQuery.list[0].loginName == "admin"
    }
//
//    def "test getRolesByUserId"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
//    def "test getRoleIdByUserId"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
    def "test findByLoginName"() {
        when:
        def result = userDao.findByLoginName(loginName)
        then:
        result.isPresent()
        result.get().loginName == loginName

        where:
        loginName << ["admin"]
    }

    def "test getOrgIdByLoginName"() {
        when:
        def result = userDao.getOrgIdByLoginName(loginName)
        then:
        result == 1L

        where:
        loginName << ["admin"]
    }
}
