package cn.com.tiza.dal.beetlsql;

import cn.com.tiza.beetlsql.Java8BeanProcessor;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;

import java.util.Map;

@UtilityClass
public class Config {
    private boolean init;

    Map<String, Map<String, String>> dbConfigMap = Maps.newHashMap();

    static {
        Map<String, String> mysql = Maps.newHashMap();
        mysql.put("userName", "root");
        mysql.put("password", "Glmysql1926");
        mysql.put("url", "jdbc:mysql://192.168.101.72:3306/gb4_dev?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        mysql.put("driver", "com.mysql.cj.jdbc.Driver");
        dbConfigMap.put("mysql", mysql);
    }

    public void dbInit(String dbType) {
        if (init) {
            return;
        }
        init = true;

        Map<String, String> db = dbConfigMap.get(dbType);
        String userName = db.get("userName");
        String password = db.get("password");
        String url = db.get("url");
        String driver = db.get("driver");

        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, userName, password);

        DBStyle mysql = new MySqlStyle();
        // sql语句放在classpagth的/sql 目录下
        SQLLoader loader = new ClasspathLoader("/sql");
        // 数据库命名跟java命名一样，所以采用DefaultNameConversion，还有一个是UnderlinedNameConversion，下划线风格的，
        NameConversion nc = new UnderlinedNameConversion();
        // 最后，创建一个SQLManager,DebugInterceptor 不是必须的，但可以通过它查看sql执行情况
        Interceptor[] inters = new Interceptor[]{new DebugInterceptor()};

        SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, inters);
        sqlManager.setDefaultBeanProcessors(new Java8BeanProcessor(sqlManager));

        SqlKit.$(sqlManager);
    }


    public static final void main(String[] args) {
        Config.dbInit("mysql");
    }
}
