package cn.com.tiza.dal.beetlsql;

import org.beetl.sql.core.SQLManager;

public class SqlKit {
    static SQLManager $;

    private SqlKit() {

    }

    /**
     * @return sqlManager
     */
    public static SQLManager $() {
        return $;
    }

    /**
     * 设置sqlMananger, 整个服务器启动只需要设置一次
     *
     * @param sqlManager
     */
    public static void $(SQLManager sqlManager) {
        $ = sqlManager;
    }

    /**
     * 获取接口内置实现
     *
     * @param mapperInterface 接口
     * @param <T>             接口实例
     * @return 接口实例
     */
    public static <T> T mapper(Class<T> mapperInterface) {
        return $().getMapper(mapperInterface);
    }
}
