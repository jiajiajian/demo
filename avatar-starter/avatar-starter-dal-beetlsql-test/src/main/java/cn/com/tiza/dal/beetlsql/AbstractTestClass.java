/**
 * 
 */
package cn.com.tiza.dal.beetlsql;

import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.beetl.sql.core.SQLReady;
import org.junit.BeforeClass;

/**
 * @author TZ0781
 *
 */
public abstract class AbstractTestClass {

	@BeforeClass
    public static void init() {
        Config.dbInit("mysql");
    }

    protected void cleanTable(String tableName) {
	    SqlKit.$().executeUpdate(new SQLReady("delete from " + tableName));
    }

    protected void printlnJson(Object obj) {
    	System.out.println(JsonMapper.defaultMapper().toJson(obj));
	}
}
