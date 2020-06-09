package cn.com.tiza.service;

import cn.com.tiza.Global;
import cn.com.tiza.api.GrampusApiClient;
import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.CmdSendResult;
import cn.com.tiza.dto.CommandDto;
import cn.com.tiza.dto.CommandMessage;
import cn.com.tiza.dto.TerminalCmd;
import cn.com.tiza.errors.GrampusErrorCode;
import cn.com.tiza.web.rest.CmdLogClient;
import cn.com.tiza.web.rest.WebSocketClient;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.LongConsumer;

import static cn.com.tiza.util.MessageUtil.getUnsignedShort;
import static cn.com.tiza.util.MessageUtil.subBytes;

/**
 * 指令下发接口
 *
 * @author TZ0781
 */
@Slf4j
@Service
public class GrampusCmdService {

    @Autowired
    private GrampusApiClient grampusApiClient;

    @Autowired
    private GrampusApiService grampusApiService;

    @Autowired
    private CmdLogClient cmdLogClient;

    @Autowired
    private WebSocketClient webSocketClient;

    public static final ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * 发送在线指令
     *
     * @param terminalType terminalType
     * @param terminalId   terminalId
     * @param cmdId        cmdId
     * @param cmdBody      cmdBody
     */
    public void cmdSendOnline(String terminalType, String terminalId, int cmdId, String cmdBody, LongConsumer consumer) {
        //下发指令
        String apiKey = this.grampusApiService.getApiKey(terminalType);
        log.info("实时指令请求报文：{}", cmdBody);
        Objects.requireNonNull(consumer, "commandId consumer can not null");
        CmdSendResult result = send(apiKey, terminalType, terminalId, cmdId, 0, cmdBody, consumer, true);
        log.info("实时指令网关响应结果：{}", result);
    }

    /**
     * 下发离线指令
     */
    public void cmdSendOffline(String terminalType, String terminalId, int cmdId, String cmdBody, LongConsumer consumer) {
        //下发离线指令
        String apiKey = this.grampusApiService.getApiKey(terminalType);
        log.info("离线指令请求报文：{}", cmdBody);
        Objects.requireNonNull(consumer, "commandId consumer can not null");
        CmdSendResult result = send(apiKey, terminalType, terminalId, cmdId, 1, cmdBody, consumer, true);
        log.info("离线指令网关响应结果：{}", result);
    }

    public void sendSendNoStore(String terminalType, String terminalId, int cmdId, String cmdBody, LongConsumer consumer) {
        String apiKey = this.grampusApiService.getApiKey(terminalType);
        send(apiKey, terminalType, terminalId, cmdId, 0, cmdBody, consumer, false);
    }


    /**
     * 向终端发送指令
     *
     * @param apiKey       apiKey
     * @param terminalType terminalType
     * @param terminalId   terminalId
     * @param cmdId        cmdId
     * @param cmdType      0：实时指令，1离线指令
     * @param cmdBody      发送指令携带的报文
     * @return 返回的checkId
     */
    private CmdSendResult send(String apiKey, String terminalType, String terminalId, int cmdId,
                               int cmdType, String cmdBody, LongConsumer consumer, boolean isStore) {

        TerminalCmd terminalCmd = new TerminalCmd();

        terminalCmd.setTerminalType(Global.assemblePrefix(terminalType));
        terminalCmd.setCmdType(cmdType);
        terminalCmd.setTimeout(5000);

        terminalCmd.setCmdBody(cmdBody);
        terminalCmd.setTerminalID(terminalId);
        terminalCmd.setCmdID(cmdId);
        terminalCmd.setCmdSerial(getUnsignedShort(subBytes(Base64.getDecoder().decode(cmdBody), 8, 2)));

        //记录指令
        if(isStore){
            Long commandId = this.recordCommand(terminalCmd);
            consumer.accept(commandId);
        }

        log.debug("send cmd {}", JsonMapper.defaultMapper().toJson(terminalCmd));
        CmdSendResult result = this.grampusApiClient.send(apiKey, terminalCmd);
        log.debug("send cmd result {}", JsonMapper.defaultMapper().toJson(result));
        if (Objects.equals(result.getIsSuccess(), Boolean.FALSE)) {
            Long userId = BaseContextHandler.getUserID();
            Map<String,Object> map = new HashMap<>();
            map.put("errorCode",result.getErrorCode());
            map.put("errorCodeStr","tstar_error_" + result.getErrorCode());
            map.put("errMsg",GrampusErrorCode.valueOf(result.getErrorCode()).getOnlyDesc());
            map.put("cmdCheckId",result.getCmdCheckId());
            map.put("isSuccess",result.getIsSuccess());
            CommandMessage commandMessage = new CommandMessage(cmdId,1);
            commandMessage.setResult(map);
            //发送weosocket通知
            inform(userId,commandMessage);
        }
        return result;
    }

    private void inform(Long userId, CommandMessage message) {
        try {
            log.info("--------------inform--------------");
            log.info("userId: {},message：{}",userId,message.toString());
            executor.submit(() -> webSocketClient.inform(userId, message));
        } catch (Exception e) {
            log.error("GrampusCmdService inform exception: {}", e.getLocalizedMessage());
        }
    }

    /**
     * 线程休眠指定的秒数，单位秒 s
     *
     * @param time 秒数
     */
    private void sleep(int time) {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            log.error("thread sleep is interrupted exception is: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 记录指令
     *
     * @param cmd terminalCmd
     * @return 指令记录id
     */
    private Long recordCommand(TerminalCmd cmd) {
        CommandDto dto = new CommandDto();
        dto.setVin(cmd.getTerminalID());
        dto.setCmdId(cmd.getCmdID());
        dto.setSerialNo(cmd.getCmdSerial());
        dto.setReqBody(cmd.getCmdBody());
        dto.setCmdType(cmd.getCmdType());
        dto.setState(cmd.getCmdType() == 1 ? 4 : null);
        return cmdLogClient.recordCommand(dto);
    }


}
