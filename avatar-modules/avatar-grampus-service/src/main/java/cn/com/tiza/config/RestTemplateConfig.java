package cn.com.tiza.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 定义RestTemplate 供Http调用
 * 需要使用HttpClient 来定义超时时间，连接池等
 *
 * @author
 */
@Configuration
@EnableConfigurationProperties({HttpProperties.class, GrampusProperties.class})
public class RestTemplateConfig {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private GrampusProperties.HttpPool pool;

    @Autowired
    public RestTemplateConfig(GrampusProperties properties) {
        this.pool = properties.getPool();
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return restTemplateBuilder.requestFactory(this::httpRequestFactory).build();
    }


    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private SSLConnectionSocketFactory sslConnectionSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());

            HostnameVerifier verifier = (String hostname, SSLSession session) -> true;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, verifier);
            return sslConnectionSocketFactory;
        } catch (Exception e) {
            return SSLConnectionSocketFactory.getSocketFactory();
        }
    }


    //@Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(pool.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(pool.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(pool.getValidateAfterInactivity());
        RequestConfig requestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间，超过抛出read timeout
                .setSocketTimeout(pool.getSocketTimeout())
                //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectTimeout(pool.getConnectTimeout())
                //从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException:
                // Timeout waiting for connection from pool
                .setConnectionRequestTimeout(pool.getConnectionRequestTimeout())
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}
