package cn.com.tiza.beetlsql;

import com.ibeetl.starter.BeetlSqlCustomize;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class Java8BeetlsqlCustomize implements BeetlSqlCustomize {

    @Override
    public void customize(SqlManagerFactoryBean sqlManager) {
        try {
            SQLManager sql = (SQLManager) sqlManager.getObject();
            sql.setDefaultBeanProcessors(new Java8BeanProcessor(sql));
        } catch (Exception e) {

        }
    }
}
