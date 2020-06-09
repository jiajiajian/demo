package cn.com.tiza.api;

import cn.com.tiza.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Grampus 指令下发,数据交换接口查询
 *
 * @author tiza
 */
@FeignClient(value = "feisi-grampus", fallback = HystrixGrampusApiFallBack.class)
public interface GrampusApiClient {

    /**
     * 终端注册
     *
     * @param terminalType
     * @param terminalId
     * @return
     */
    @PostMapping("/terminals/{terminalType}/{terminalId}/{apiKey}")
    RestResult register(@PathVariable("apiKey") String apiKey,
                        @PathVariable("terminalType") String terminalType,
                        @PathVariable("terminalId") String terminalId);

    /**
     * 终端删除
     *
     * @param terminalType
     * @param terminalId
     * @return
     */
    @DeleteMapping("/terminals/{terminalType}/{terminalId}/{apiKey}")
    RestResult unregister(@PathVariable("apiKey") String apiKey,
                          @PathVariable("terminalType") String terminalType,
                          @PathVariable("terminalId") String terminalId);


    /**
     * 同一个指令在没有响应之前则不允许下发，离线指令和定时指令可以更新
     */
    @PostMapping(value = "/cmd/{apiKey}")
    CmdSendResult send(@PathVariable("apiKey") String apiKey,
                       @RequestBody TerminalCmd terminalCmd);

    @PostMapping(value = "/cmd/timing/{apiKey}")
    CmdSendResult sendTimer(@PathVariable("apiKey") String apiKey,
                            @RequestBody TerminalTimingCmd terminalTimingCmd);

    /**
     * 根据checkId查询指令响应结果
     */
    @GetMapping(value = "/cmd/cmd_response/{cmdCheckId}/{apiKey}")
    CmdCheckResult cmdChecker(@PathVariable("cmdCheckId") String cmdCheckId, @PathVariable("apiKey") String apiKey);

    /**
     * 远程升级指令
     *
     * @param terminalId
     * @param data
     * @return
     */
    @PostMapping("/cmd/{terminalId}/upgrade/{apiKey}")
    CmdSendResult upgradeCmdSend(@PathVariable("apiKey") String apiKey,
                                 @PathVariable("terminalId") String terminalId,
                                 @RequestBody Map<String, Object> data);

    /**
     * 创建查询器
     *
     * @param apiKey
     * @return
     */
    @PostMapping("/data/{apiKey}")
    RestCreateDataQueryResult createDataQuery(@PathVariable("apiKey") String apiKey,
                                              @RequestBody QueryCommand command);

    /**
     * 创建原始表查询器
     *
     * @param apiKey  apiKey
     * @param command command
     * @return queryId
     */
    @PostMapping("/data/{apiKey}/row")
    RestCreateDataQueryResult createDataQueryRow(@PathVariable("apiKey") String apiKey,
                                                 @RequestBody QueryCommand command);


    /**
     * 创建轨迹表查询器
     *
     * @param apiKey  apiKey
     * @param command command
     * @return queryId
     */
    @PostMapping("/data/{apiKey}/tracker")
    RestCreateDataQueryResult createDataQueryTracker(@PathVariable("apiKey") String apiKey,
                                                     @RequestBody QueryCommand command);

    /**
     * 查询
     *
     * @param queryId
     * @return
     */
    @GetMapping("/data/{queryId}/{apiKey}")
    RestDataQueryResult dataQuery(@PathVariable("apiKey") String apiKey,
                                  @PathVariable("queryId") Integer queryId);

    /**
     * 查询指定行数的数据
     *
     * @param apiKey  apiKey
     * @param queryId queryId
     * @param row     row
     * @return
     */
    @GetMapping("/data/{queryId}/{apiKey}/{row}")
    RestDataQueryResult dataQuery(@PathVariable("apiKey") String apiKey,
                                  @PathVariable("queryId") Integer queryId,
                                  @PathVariable("row") Integer row);

    /**
     * 删除查询器
     *
     * @param queryId
     */
    @DeleteMapping("/data/{queryId}/{apiKey}")
    void closeDataQuery(@PathVariable("apiKey") String apiKey,
                        @PathVariable("queryId") Integer queryId);

    /**
     * 根据terminalType自动得到 HBase表名
     *
     * @param vin          机器序列号
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param cmdId        指令id（//cmdID默认0 查询所有工况数据，保留 2 实时数据、 3 补发数据）
     * @param terminalType 终端类型
     * @return 查询器
     */
    default QueryCommand buildCommandTerminalType(String vin, Long startTime, Long endTime, Integer cmdId) {
        return QueryCommand.builder()
                .terminalID(vin)
                .startTime(startTime)
                .endTime(endTime)
                .cmdID(cmdId)
                .build();
    }

    /**
     * 需要自己传入表名
     */
    default QueryCommand buildCommand(String vin, Long startTime, Long endTime, Integer cmdId, String tableName) {
        return QueryCommand.builder()
                .terminalID(vin)
                .startTime(startTime)
                .endTime(endTime)
                .cmdID(cmdId)
                .tableName(tableName)
                .build();
    }

}
