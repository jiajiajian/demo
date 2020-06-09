package cn.com.tiza.dao;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.dal.beetlsql.AbstractTestClass;
import cn.com.tiza.dal.beetlsql.SqlKit;
import cn.com.tiza.domain.User;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.UserQuery;
import cn.com.tiza.web.rest.vm.UserVM;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

public class UserDaoDBTest extends AbstractTestClass {

    private UserDao userDao = SqlKit.mapper(UserDao.class);

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     *
     */
    @Test
    public void insert() {
        String pwd = md5DigestAsHex(Constants.DEFAULT_PWD.getBytes());
        System.out.println(pwd);
        User user = new User();
//        user.setLoginName("admin");
//        user.setLoginPassword(passwordEncoder.encode(pwd));
//        user.setUserType(UserType.ADMIN);
//        userDao.insert(user);
    }

    @Test
    public void pageQuery() {
        UserQuery query = new UserQuery();
//        query.setOrganizationId(1L);
        PageQuery<UserVM> pageQuery = query.toPageQuery();
        userDao.pageQuery(pageQuery);
        assertThat(pageQuery.getTotalRow(), is(1L));
        assertThat(pageQuery.getList().get(0).getLoginName(), is("admin"));
    }
}