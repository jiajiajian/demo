package cn.com.tiza.security.auth;

/**
 *
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
