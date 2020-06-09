package cn.com.tiza.domain;

import lombok.Data;

/**
 * @author villas
 * @since 2019/5/13 13:45
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String password;
}
