package cn.com.tiza.cmd.command.service;


import cn.com.tiza.cmd.CommandMsg;
import cn.com.tiza.cmd.command.dao.CommandDao;
import cn.com.tiza.cmd.command.domain.Command;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.CommandDto;
import cn.com.tiza.dto.CommandMessage;
import cn.com.tiza.service.CmdService;
import cn.com.tiza.web.rest.WebSocketClient;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service
 * gen by beetlsql 2020-05-14
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CommandService {

    @Autowired
    private CommandDao commandDao;

    @Autowired
    private CmdService cmdService;

    @Autowired
    private WebSocketClient webSocketClient;

    public static final ExecutorService executor = Executors.newFixedThreadPool(4);


    public Optional<Command> get(Long id) {
        return Optional.ofNullable(commandDao.single(id));
    }

    public void sendRetryCmd(CommandMsg msg){
        Command command = this.getCommand(msg);
        String reqBody = command.getReqBody();
        cmdService.sendRetryCommand(command.getVin(), command.getCmdId(), reqBody);
    }

    public int updateStatus(CommandMsg msg, Command command) {
        return commandDao.createLambdaQuery()
                .andEq(Command::getVin, msg.getVin())
                .andEq(Command::getCmdId, msg.getCmdId())
                .andEq(Command::getDate, msg.getDate())
                .andEq(Command::getSerialNo, msg.getSerialNo())
                .updateSelective(command);
    }

    public void updateStatusTimeOut(CommandMsg msg){
        Command command = new Command();
        command.setState(2);
        commandDao.createLambdaQuery()
                .andEq(Command::getVin, msg.getVin())
                .andEq(Command::getCmdId, msg.getCmdId())
                .andEq(Command::getDate, msg.getDate())
                .andEq(Command::getSerialNo, msg.getSerialNo())
                .updateSelective(command);
    }

    public Command getCommand(CommandMsg msg){
        return commandDao.createLambdaQuery()
                .andEq(Command::getVin, msg.getVin())
                .andEq(Command::getCmdId, msg.getCmdId())
                .andEq(Command::getDate, msg.getDate())
                .andEq(Command::getSerialNo, msg.getSerialNo())
                .single();
    }

    public Long insert(CommandDto dto){
        Command command = new Command();
        command.setVin(dto.getVin());
        command.setCmdId(dto.getCmdId());
        command.setCmdType(dto.getCmdType());
        command.setSerialNo(dto.getSerialNo());
        command.setRemark(dto.getRemark());
        command.setState(dto.getState());
        command.setReqBody(dto.getReqBody());
        command.setDate(Integer.valueOf(DateFormatUtil.formatDate("yyyyMMdd", System.currentTimeMillis())));
        command.setSendTime(System.currentTimeMillis());
        command.setUserId(BaseContextHandler.getUserID());
        command.setOrgId(BaseContextHandler.getOrgId());
        command.setIpAddress(BaseContextHandler.getIpAddress());
        command.setOperateTime(System.currentTimeMillis());
        commandDao.insert(command, true);
        return command.getId();
    }

    public void delete(Long id){
        commandDao.deleteById(id);
    }

    public void update(Long id, Command command){
        get(id).ifPresent(entity->{
            command.setId(id);
            commandDao.updateTemplateById(command);
        });
    }

    public int getSerialNo(){
        String date = DateFormatUtil.formatDate("yyyyMMdd", System.currentTimeMillis());
        Integer serialNo = commandDao.getMaxSerialNoByDay(Integer.parseInt(date));
        return Objects.isNull(serialNo) || (serialNo + 1) >= 65535 ? 0 : serialNo + 1;
    }

    public void inform(Long userId, CommandMessage message) {
        try {
            log.info("--------------inform--------------");
            executor.submit(() -> webSocketClient.inform(userId, message));
        } catch (Exception e) {
            log.error("KafkaCommandListener inform exception: {}", e.getLocalizedMessage());
        }
    }

}
