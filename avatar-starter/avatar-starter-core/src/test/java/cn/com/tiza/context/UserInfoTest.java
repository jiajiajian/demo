package cn.com.tiza.context;

import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInfoTest {

    @Test
    public void print() {
        UserInfo user = UserInfo.builder()
                .id(1L)
                .loginName("admin")
                .realName("23")
                .orgId(1L)
                .ipAddress("192.168.103.108")
                .userAgent("IE")
                .build();

        String u = JsonMapper.defaultMapper().toJson(user);
        System.out.println(u);
        JsonMapper.defaultMapper().fromJson(u, UserInfo.class);
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        int x= 1;
        x /= 10;
        System.out.println(x);

    }
}