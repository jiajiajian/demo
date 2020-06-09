package cn.com.tiza.grampus;

import cn.com.tiza.Global;
import cn.com.tiza.config.GrampusProperties;
import cn.com.tiza.dto.*;
import cn.com.tiza.errors.GrampusErrorCode;
import cn.com.tiza.errors.GrampusErrorException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TStar 平台调用
 *
 * @author tiza
 */
@Slf4j
@Component
public class GrampusClient {

    private GrampusProperties.Grampus properties;

    private RestTemplate restTemplate;

    @Autowired
    public GrampusClient(GrampusProperties properties, RestTemplate restTemplate) {
        this.properties = properties.getGrampus();
        this.restTemplate = restTemplate;
    }

    private HttpHeaders addHeader(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Global.HEADER_API_KEY, apiKey);
        return headers;
    }

    /**
     * 向大数据平台注册
     *
     * @param protocolType type
     * @param vin          vin
     */
    @SuppressWarnings("unchecked")
    public RestResult register(String apiKey, String protocolType, String vin) {
        Objects.requireNonNull(protocolType, "终端类型为空，向大数据平台注册失败！");
        // 1.需要调用注册车辆接口在大数据方注册车辆
        List<Map<String, String>> body = ImmutableList.of(ImmutableMap.of("terminalID", vin));
        HttpEntity<List<Map<String, String>>> entity = new HttpEntity(body, addHeader(apiKey));
        RestResult result = restTemplate.postForObject(getTerminalUrl() + protocolType, entity, RestResult.class);
        Objects.requireNonNull(result, "注册终端到平台，响应为空！");
        return result;
    }

    /**
     * 注销一个已经注册的终端
     *
     * @param apiKey
     * @param vin
     * @param type
     */
    @SuppressWarnings("unchecked")
    public RestResult unRegister(String apiKey, String type, String vin) {
        Objects.requireNonNull(type, "终端类型为空，向大数据平台注册失败！");
        HttpEntity<String> entity = new HttpEntity(null, addHeader(apiKey));
        try {
            ResponseEntity<RestResult> response = restTemplate.exchange(getTerminalUrl() + type + "/" + vin,
                    HttpMethod.DELETE, entity, RestResult.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("deleteToPlatform exception: " + e.getMessage());
            throw new GrampusErrorException(GrampusErrorCode.E9999);
        }
    }

    public RestCreateDataQueryResult createDataQueryRaw(String apiKey, QueryCommand command) {
       // command.setTableName(this.getTableNameOfRaw(command.getTerminalType()));
        return createDataQuery(apiKey, command);
    }

    public RestCreateDataQueryResult createDataQueryTrack(String apiKey, QueryCommand command) {
        //command.setTableName(this.getTableNameOfTrack(command.getTerminalType()));
        return createDataQuery(apiKey, command);
    }

    public RestCreateDataQueryResult createDataQuery(String apiKey, QueryCommand command) {
        log.debug("Create querier request is {}", command.toString());
        // http请求
        String url = getDataQueryUrl();
        @SuppressWarnings("unchecked")
        HttpEntity<String> entity = new HttpEntity(command, addHeader(apiKey));
        try {
            return restTemplate.postForObject(url, entity, RestCreateDataQueryResult.class);
        } catch (Exception e) {
            log.error("method create data query error :" + e.getMessage(), e);
            throw new GrampusErrorException(GrampusErrorCode.E9999);
        }
    }

    public RestDataQueryResult dataQuery(String apiKey, int queryId, Integer rows) {
        String url = getDataQueryUrl() + "/" + queryId + "?rows=" + rows;
        @SuppressWarnings("unchecked")
        HttpEntity<String> entity = new HttpEntity(null, addHeader(apiKey));
        try {
            ResponseEntity<RestDataQueryResult> response = restTemplate.exchange(url, HttpMethod.GET, entity, RestDataQueryResult.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("HTTP请求失败 :" + e.getMessage(), e);
            throw new GrampusErrorException(GrampusErrorCode.E9999);
        }
    }

    public void closeDataQuery(String apiKey, int queryId) {
        @SuppressWarnings("unchecked")
        HttpEntity<String> entity = new HttpEntity(null, addHeader(apiKey));
        try {
            // http请求
            restTemplate.exchange(getDataQueryUrl() + "/" + queryId, HttpMethod.DELETE, entity, RestResult.class);
        } catch (Exception e) {
            log.error("HTTP请求失败 :" + e.getMessage(), e);
            throw new GrampusErrorException(GrampusErrorCode.E9999);
        }
    }

    private String getDataQueryUrl() {
        return properties.getDataInterface() + "/data_querys";
    }

    private String getTerminalUrl() {
        return properties.getDataInterface() + "/terminals/" + properties.getTablePrefix();
    }


    private String getTableNameOfTrack(String terminalType) {
        return properties.getTablePrefix() + terminalType + properties.getTrackData();
    }

    private String getTableNameOfRaw(String terminalType) {
        return properties.getTablePrefix() + terminalType + properties.getRawData();
    }

    public CmdSendResult cmdSend(TerminalCmd cmd, String apiKey) {
        String url = this.getIssueCmdUrl();
        log.info("send cmd url is {}, request json is {}", url, cmd);
        @SuppressWarnings("unchecked")
        HttpEntity<TerminalCmd> entity = new HttpEntity(cmd, addHeader(apiKey));
        CmdSendResult response = restTemplate.postForObject(url, entity, CmdSendResult.class);
        log.info("send cmd response is {}", response);
        return response;
    }

    public CmdCheckResult checkResult(String checkId, String apiKey) {
        String url = this.getCheckResponseUrl(checkId);
        log.info("cmd result url is {}", url);
        @SuppressWarnings("unchecked")
        HttpEntity<TerminalCmd> entity = new HttpEntity(null, addHeader(apiKey));
        try {
            ResponseEntity<CmdCheckResult> result = restTemplate.exchange(url, HttpMethod.GET, entity, CmdCheckResult.class);
            return result.getBody();
        } catch (Exception e) {
            log.error("cmd result exception: " + e.getMessage());
            throw new GrampusErrorException(GrampusErrorCode.E9999);
        }

    }

    private String getIssueCmdUrl() {
        return properties.getCmdInterface() + "/cmd_send";
    }

    private String getCheckResponseUrl(String checkId) {
        return properties.getCmdInterface() + "/cmd_response/" + checkId;
    }


}
