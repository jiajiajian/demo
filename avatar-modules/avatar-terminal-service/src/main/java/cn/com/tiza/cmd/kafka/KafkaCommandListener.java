package cn.com.tiza.cmd.kafka;

import cn.com.tiza.cmd.CommandMsg;
import cn.com.tiza.cmd.command.domain.Command;
import cn.com.tiza.cmd.command.service.CommandService;
import cn.com.tiza.cmd.kafka.entity.TStarData;
import cn.com.tiza.cmd.redis.RedisService;
import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dto.CommandMessage;
import cn.com.tiza.protocol.ICmd;
import cn.com.tiza.protocol.one.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 指令结果消费
 *
 * @author villas
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "application.kafka.bootstrapServers")
public class KafkaCommandListener {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private ApplicationProperties properties;


    /**
     * 消费kafka的消息
     *
     * @param record record
     */
    @KafkaListener(topics = "#{'${application.kafka.cmd_topics}'.split(',')}")
    public void listen(ConsumerRecord<String, TStarData> record, Acknowledgment ack) {
        if (Objects.isNull(record.value())) {
            return;
        }
        TStarData data = record.value();
        String cmdId = data.getCmdID();
        ack.acknowledge();
        if (!CMD_ID_SET.contains(Integer.parseInt(cmdId))) {
            return;
        }
        log.info("====> cmdId is {}", cmdId);
        CommandMsg msg = redisService.jsonToObj(new String(data.getMsgBody()), CommandMsg.class);
        Command commandDb = commandService.getCommand(msg);
        if (commandDb == null) {
            return;
        }

        log.info("------------> msg is {}", msg);
        if (msg.getState() == 0 || msg.getState() == 3) {
            if (properties.getCommand().isEnableRetry() && this.isOnline(commandDb)) {
                //清除重试缓存
                redisService.deleteRetryCache(msg);
            }
            this.successProcess(msg, commandDb);
        } else if (msg.getState() == 7) {

            //如果是实时指令开启 重试、超时判定
            if (properties.getCommand().isEnableRetry() && this.isOnline(commandDb)) {
                redisService.retryProcess(msg);
            }
            //数据库更新状态
            Command commandDto = new Command();
            commandDto.setState(7);
            commandDto.setResTime(msg.getTime());
            commandDto.setResBody(msg.getMsgBody());
            commandService.updateStatus(msg, commandDto);
        }

    }

    private static final ICmd[] CMD_ARR = {new Cmd_02(), new Cmd_03(), new Cmd_04(), new Cmd_05(), new Cmd_10(),
            new Cmd_18(), new Cmd_11(), new Cmd_2C(), new Cmd_40(), new Cmd_48()};

    private static final Set<Integer> CMD_ID_SET = Arrays.stream(CMD_ARR).map(ICmd::getCmdId).collect(Collectors.toSet());

    private void successProcess(CommandMsg msg, Command commandDb) {

        Arrays.stream(CMD_ARR)
                .filter(c -> c.getCmdId() == msg.getCmdId())
                .map(cmd -> cmd.parseBody(msg.getMsgBody()))
                .findFirst()
                .ifPresent(cmdBody -> {
                    int state = cmdBody.getState();
                    //数据库更新状态
                    Command commandDto = new Command();
                    commandDto.setState(state);
                    commandDto.setResTime(msg.getTime());
                    commandDto.setResBody(msg.getMsgBody());
                    Map<String, Object> successMap = cmdBody.getSuccessMap();
                    if (!Objects.isNull(successMap)) {
                        commandDto.setResJsonBody(redisService.objToJson(successMap));
                    }
                    commandService.updateStatus(msg, commandDto);

                    log.info("||||||||||||||||更新数据库成功||||||||||");

                    //b.如果是实时指令，则需要通知页面
                    if (this.isOnline(commandDb)) {
                        CommandMessage message = new CommandMessage(msg.getCmdId(), state);
                        message.setResult(successMap);
                        commandService.inform(commandDb.getUserId(), message);
                    }
                });
    }


    private boolean isOnline(Command command) {
        return command.getCmdType() == 0;
    }


}
