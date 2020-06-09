package cn.com.tiza.service;

import cn.com.tiza.Global;
import cn.com.tiza.api.GrampusApiClient;
import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.dto.RestCreateDataQueryResult;
import cn.com.tiza.dto.RestDataQueryResult;
import cn.com.tiza.dto.RestDataRecord;
import cn.com.tiza.errors.GrampusErrorCode;
import cn.com.tiza.errors.GrampusErrorException;
import cn.com.tiza.util.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author TZ0781
 */
@Slf4j
@Service
public class GrampusDataQueryService {

    @Autowired
    private GrampusApiClient grampusApiClient;

    @Autowired
    private GrampusApiService grampusApiService;

    /**
     * 调用TStar API查询HBase数据
     *
     * @param terminalType 获取apikey的参数
     * @param vin          TerminalID/VIN
     * @param startTime    开始时间，时间戳
     * @param endTime      结束时间，时间戳
     * @param cmdId        指令类型
     * @param tableName    HBase表名
     * @param all          是否所有数据
     * @param raw          是否原始报文
     * @param row          一次返回指定行数的数据
     * @param mapper       转换RestDataRecord
     * @param handler      处理
     * @param <T>          中间类型
     */
    public <T> void queryRangeData(String terminalType, String vin, Long startTime, Long endTime, Integer cmdId,
                                   String tableName, boolean all, boolean raw, int row,
                                   Predicate<? super RestDataRecord> filter,
                                   Function<RestDataRecord, T> mapper, Consumer<T> handler) {

        String apiKey = grampusApiService.getApiKey(terminalType);
        RestCreateDataQueryResult result = grampusApiClient.createDataQuery(apiKey,
                grampusApiClient.buildCommand(vin, startTime, endTime, cmdId == null ? 0 : cmdId, tableName));
        Objects.requireNonNull(result, "创建查询器返回结果为空");

        if (result.getIsSuccess()) {
            int queryId = result.getQueryId().intValue();
            RestDataQueryResult queryResult;
            do {
                queryResult = grampusApiClient.dataQuery(apiKey, queryId, row);
                if (queryResult != null && queryResult.getIsSuccess()) {
                    Stream<RestDataRecord> stream = queryResult.getRecords()
                            .stream();
                    if (filter != null) {
                        stream = stream.filter(filter);
                    }
                    stream.peek(record -> {
                        if (raw) {
                            record.setBody(EncodeUtil.encodeHex(EncodeUtil.decodeBase64(record.getBody())));
                        } else {
                            record.setBody(new String(EncodeUtil.decodeBase64(record.getBody())));
                        }
                    }).map(mapper).forEach(handler);
                } else {
                    throw new GrampusErrorException(GrampusErrorCode.valueOf(result.getErrorCode()));
                }
            } while (all && !queryResult.getIsEnd());
            grampusApiClient.closeDataQuery(apiKey, queryId);
        } else {
            log.error("------------ query params vin {} startTime {} endTime {} cmdId {} tableName {}",
                    vin, startTime, endTime, cmdId, tableName);

            log.error("------------ result is error ! errorCode {}, queryId {}",
                    result.getErrorCode(), result.getQueryId());

            throw new GrampusErrorException(GrampusErrorCode.valueOf(result.getErrorCode()));
        }
    }

    /**
     * 查询一次或全部(all == ?)、指定类型（cmd == ?）、符合要求（filter）的数据
     *
     */
    public <T> void queryRangeData(String terminalType, String vin, Long startTime, Long endTime, Integer cmdId,
                                   String tableName, boolean all, boolean raw,
                                   Predicate<? super RestDataRecord> filter,
                                   Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime, cmdId, tableName, all, raw, 1000, filter, mapper, handler);
    }

    /**
     * 查询一次或全部(all == ?)、所有类型（cmd == 0）数据
     */
    public <T> void queryRangeData(String terminalType, String vin, Long startTime, Long endTime,
                                   String tableName, boolean all, boolean raw,
                                   Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime, 0, tableName, all, raw, null, mapper, handler);
    }

    /**
     * 调用TStar API查询HBase数据，默认查询所有指令
     *
     */
    public <T> void queryRawData(String terminalType, String vin, Long startTime, Long endTime, Integer cmdId, boolean all,
                                 Function<RestDataRecord, T> mapper, Consumer<T> handler) {

        queryRangeData(terminalType, vin, startTime, endTime, cmdId,
                Global.rawTableName(terminalType), all, true, null, mapper, handler);
    }

    /**
     * 调用TStar API查询HBase数据，默认查询所有指令
     *
     */
    public <T> void queryRawData(String terminalType, String vin, Long startTime, Long endTime, Integer cmdId,
                                 Function<RestDataRecord, T> mapper, Consumer<T> handler) {

        queryRawData(terminalType, vin, startTime, endTime, cmdId, true, mapper, handler);
    }

    /**
     * 调用TStar API查询HBase数据，默认查询所有指令
     *
     */
    public <T> void queryTrackData(String terminalType, String vin,
                                   Long startTime, Long endTime, boolean all,
                                   Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime,
                Global.trackTableName(terminalType), all, false, mapper, handler);
    }

    /**
     * 调用TStar API查询HBase数据，默认查询所有指令
     *
     */
    public <T> void queryTrackData(String terminalType, String vin,
                                   Long startTime, Long endTime,
                                   Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryTrackData(terminalType, vin, startTime, endTime, true, mapper, handler);
    }

    /**
     * 查询HBase数据,并执行过滤操作
     *
     */
    public <T> void queryTrackFilterData(String terminalType, String vin, Long startTime, Long endTime,
                                         Predicate<? super RestDataRecord> filter,
                                         Function<RestDataRecord, T> mapper,
                                         Consumer<T> handler) {
        queryTrackFilterData(terminalType, vin, startTime, endTime, 0, true, filter, mapper, handler);
    }

    /**
     * 查询HBase数据,并执行过滤操作
     *
     */
    public <T> void queryTrackFilterData(String terminalType, String vin, Long startTime, Long endTime,
                                         Integer cmdId, boolean all,
                                         Predicate<? super RestDataRecord> filter,
                                         Function<RestDataRecord, T> mapper,
                                         Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime, cmdId, Global.trackTableName(terminalType),
                all, false, filter, mapper, handler);
    }

    /**
     * 查询全部（all==true）、所有类型（cmd == 0）的数据
     */
    public <T> void queryAllTotalData(String terminalType, String vin, Long startTime, Long endTime,
                                      String tableName, boolean raw,
                                      Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime, tableName, true, raw, mapper, handler);
    }

    /**
     * 查询全部（all==true）、指定类型（cmd == ?）的数据
     */
    public <T> void queryAllTypeData(String terminalType, String vin, Long startTime, Long endTime, Integer cmdId,
                                     String tableName, boolean raw,
                                     Function<RestDataRecord, T> mapper, Consumer<T> handler) {
        queryRangeData(terminalType, vin, startTime, endTime, cmdId, tableName, true, raw, null, mapper, handler);
    }

}
