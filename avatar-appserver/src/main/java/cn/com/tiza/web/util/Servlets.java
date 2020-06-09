package cn.com.tiza.web.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * 获取请求信息工具类
 */
public class Servlets {


    public static String getUserAgent(ServerHttpRequest request) {
        String agent = request.getHeaders().getFirst("User-Agent");
        if (StringUtils.hasText(agent)) {
            StringTokenizer st = new StringTokenizer(agent, ";");
            String browser = st.nextToken();
            if (browser.length() > 10) {
                return browser.substring(0, 10);
            }
            return browser;
        }
        return null;
    }

    public static String getClientIP(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ipAddress = null;
        ipAddress = headers.getFirst("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
            if ("127.0.0.1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // "***.***.***.***".length()
        if (ipAddress != null && ipAddress.length() > 15) {
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
