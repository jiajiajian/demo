package cn.com.tiza.service;

import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.dto.NoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信发送工具类
 * <pre>
 * 应用名称：tiza_product_display
 * APP_Key：7HEqUso3mN9O7xM74CI3SLSHM7FW
 * APP_Secret：1y3wa84Ni699pgnUF5p8B41bV9Nh
 * APP接入地址：https://api.rtc.huaweicloud.com:10443
 * 签名通道号：10690400999303551
 * 模板ID： ffc234029866478c93e982b107f625fb
 * </pre>
 *
 * @author tiza
 */
@Slf4j
@Component
public class SendSmsClient {
    @Autowired
    private ApplicationProperties properties;

    /**
     * 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
     */
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    /**
     * 无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
     */
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    public void sendSms(NoticeDTO noticeDto) throws Exception {
        ApplicationProperties.Sms sms = properties.getSms();
        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        //短信接收人号码
        String receiver = formatReceivers(noticeDto.getReceivers());
        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";
        //模板变量
        String templateParas = noticeDto.getTemplateParas();
        log.info(templateParas);
        //请求Body,不携带签名名称时,signature请填null
        String templateId;
        if (AlarmType.ALARM.equals(noticeDto.getAlarmType())) {
            templateId = sms.getAlarmTemplateId();
        } else if (AlarmType.FAULT.equals(noticeDto.getAlarmType())) {
            templateId = sms.getFaultTemplateId();
        } else {
            templateId = sms.getFenceTemplateId();
        }
        String body = buildRequestBody(sms.getSender(), receiver, templateId, templateParas, statusCallBack, sms.getSignature());
        if (null == body || body.isEmpty()) {
            log.info("body is null.");
            return;
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(sms.getAppKey(), sms.getAppSecret());
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            log.info("wsse header is null.");
            return;
        }

        //如果JDK版本是1.8,可使用如下代码
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        //请求方法POST
        HttpResponse response = client.execute(RequestBuilder.create("POST")
                .setUri(sms.getUrl())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .setEntity(new StringEntity(body)).build());

        //打印响应头域信息
        log.info("header: {}", response.toString());
        //打印响应消息实体
        log.info("body: {}", EntityUtils.toString(response.getEntity()));
    }

    /**
     * 构造请求Body体
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature         | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            log.info("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }

        return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            log.info("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //Created
        String time = sdf.format(new Date());
        //Nonce
        String nonce = UUID.randomUUID().toString().replace("-", "");

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        //PasswordDigest
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes());
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    public String formatReceivers(List<String> phones) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < phones.size(); i++) {
            if (i == phones.size() - 1) {
                stringBuilder.append("+86").append(phones.get(i));
            } else {
                stringBuilder.append("+86").append(phones.get(i)).append(",");
            }
        }
        return stringBuilder.toString();
    }
}
