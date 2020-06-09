package cn.com.tiza.service;

import cn.com.tiza.Global;
import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dto.ForwardJsonBody;
import cn.com.tiza.dto.RestDataRecord;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author villas
 * @since 2019/6/15 11:19
 */
@Slf4j
@Service
public class PlatformService {

    @Autowired
    private GrampusDataQueryService dataQueryService;

    @Autowired
    private ApplicationProperties properties;

    private static final int TOTAL = 2000;
    /**
     * 应答失败
     */
    private static final int FAIL_RESPONSE = 218;
    /**
     * 超时失败
     */
    private static final int FAIL_TIMEOUT = 219;

    /**
     * 导出失败的数据
     *
     * @param vin       vin
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param cmdId     cmdId
     * @return 失败数据集
     */
    public List<RestDataRecord> queryRangeData(String vin, Long startTime, Long endTime, Integer cmdId, Long taskId) {
        List<RestDataRecord> res = new LinkedList<>();
        String tableName = Global.fwpTableName(properties.getTstar().getTerminalType(), taskId);
        log.info("export record the tableName is {}", tableName);
        if (cmdId != null && cmdId != 0) {
            dataQueryService.queryAllTotalData(taskId.toString(), vin, startTime, endTime, tableName,
                    false, this::bodyParse, res::add);
        } else {
            dataQueryService.queryAllTypeData(taskId.toString(), vin, startTime, endTime, FAIL_RESPONSE, tableName,
                    false, this::bodyParse, res::add);
            dataQueryService.queryAllTypeData(taskId.toString(), vin, startTime, endTime, FAIL_TIMEOUT, tableName,
                    false, this::bodyParse, res::add);

            res.sort(Comparator.comparingLong(RestDataRecord::getTime).reversed());
        }
        return res;
    }

    /**
     * 查询失败的数据
     *
     * @param vin       vin
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param cmdId     cmdId
     * @return 失败数据集
     */
    public List<RestDataRecord> queryPartialData(String vin, Long startTime, Long endTime, Integer cmdId, Long taskId) {
        List<RestDataRecord> res = new ArrayList<>(TOTAL);
        String tableName = Global.fwpTableName(properties.getTstar().getTerminalType(), taskId);
        log.info("query failed data tableName is {}", tableName);
        if (cmdId != null && cmdId != 0) {
            dataQueryService.queryRangeData(taskId.toString(), vin, startTime, endTime, cmdId, tableName, false,
                    false, TOTAL, null, this::bodyParse, res::add);
        } else {
            dataQueryService.queryRangeData(taskId.toString(), vin, startTime, endTime, FAIL_RESPONSE, tableName, false,
                    false, 1000, null, this::bodyParse, res::add);
            int num = TOTAL - res.size();
            dataQueryService.queryRangeData(taskId.toString(), vin, startTime, endTime, FAIL_TIMEOUT, tableName, false,
                    false, num, null, this::bodyParse, res::add);

            res.sort(Comparator.comparingLong(RestDataRecord::getTime).reversed());
        }
        return res;
    }

    /**
     * 导出成功的报文
     *
     * @param vin       vin
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param cmdId     cmdId
     */
    public void queryRangeDataSuccess(Long taskId, String vin, Long startTime, Long endTime, String tableName,
                                      Integer cmdId, Consumer<? super RestDataRecord> handler) {
        log.info("query successful data tableName is {}", tableName);
        Predicate<RestDataRecord> filter = null;
        if (cmdId == null || cmdId == 0) {
            filter = tem -> tem.getCmdID() != FAIL_RESPONSE && tem.getCmdID() != FAIL_TIMEOUT;
        }
        dataQueryService.queryRangeData(taskId.toString(), vin, startTime, endTime, cmdId, tableName, true, true,
                filter, t -> t, handler);
    }


    private RestDataRecord bodyParse(RestDataRecord record) {
        ForwardJsonBody jsonBody = JsonMapper.defaultMapper().fromJson(record.getBody(), ForwardJsonBody.class);
        record.setRt(jsonBody.getRt());
        //用原始报文替换json字符串
        record.setBody(jsonBody.getBody());
        return record;
    }
}
