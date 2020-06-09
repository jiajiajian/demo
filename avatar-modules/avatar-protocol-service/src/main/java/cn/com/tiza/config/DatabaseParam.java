package cn.com.tiza.config;

import lombok.Data;

/**
 * 数据库配置信息
 *
 * @author villas
 */
@Data
public class DatabaseParam {
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;

    public DatabaseParam(String driverClassName, String jdbcUrl, String username, String password) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }
}
