package cn.com.tiza.service;

import cn.com.tiza.cmd.command.service.CommandService;
import cn.com.tiza.constant.GeneralParamEnum;
import cn.com.tiza.dao.DicItemDao;
import cn.com.tiza.dao.FunctionSetItemLockDao;
import cn.com.tiza.dao.TerminalDao;
import cn.com.tiza.dao.TerminalTestDao;
import cn.com.tiza.domain.DicItem;
import cn.com.tiza.domain.FunctionSetItemLock;
import cn.com.tiza.domain.TerminalTest;
import cn.com.tiza.dto.CommandDto;
import cn.com.tiza.service.dto.AccParam;
import cn.com.tiza.service.dto.CmdQuery;
import cn.com.tiza.service.dto.GeneralDto;
import cn.com.tiza.service.dto.TerminalParam;
import cn.com.tiza.util.CmdConstant;
import cn.com.tiza.util.MessageUtil;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * @author villas
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CmdService {

    @Autowired
    private GrampusCmdService grampusCmdService;

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private FunctionSetItemLockDao functionSetItemLockDao;

    @Autowired
    private DicItemDao dicItemDao;

    @Autowired
    private TerminalTestDao testDao;

    @Autowired
    private CommandService commandService;


    private static final int URL_BYTE_MAX_LEN = 200;

    private static final int VIN_CODE_LEN = 17;

    private static final String DOT = "\\.";

    public void location(String terminal, int cmd) {
        this.sendNoReqBody(terminal, cmd, true, "location");
    }

    private LongConsumer getTestConsumer(String terminal, String itemCode) {
        return commandId -> {
            TerminalTest single = testDao.createLambdaQuery().andEq(TerminalTest::getTerminalCode, terminal)
                    .andEq(TerminalTest::getCode, itemCode)
                    .single();
            if (single == null) {
                TerminalTest test = new TerminalTest(terminal, commandId);
                test.setCode(itemCode);
                testDao.insert(test);
            } else {
                single.setCommandId(commandId);
                testDao.updateById(single);
            }
        };
    }

    public void working(String terminal, int cmd) {
        this.sendNoReqBody(terminal, cmd, true, "working");
    }

    public void upgrade(String terminal, int cmd, String url) {
        byte[] bytes = url.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= URL_BYTE_MAX_LEN) {
            throw new BadRequestAlertException("upgrade url length is too long", url, "upgrade.url.too.long");
        }
        ByteBuffer buffer = ByteBuffer.allocate(1 + bytes.length);
        buffer.put((byte) 0);
        buffer.put(bytes);
        this.sendCmdWithBody(terminal, cmd, buffer.array());
    }

    public void trace(String terminal, int cmd, int time) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short) time);
        this.sendCmdWithBody(terminal, cmd, buffer.array());
    }

    public void releaseAlarm(String terminal, int cmd) {
        this.sendNoReqBody(terminal, cmd, false, null);
    }

    public void accParam(String terminal, int cmd, AccParam acc) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putShort((short) acc.getOpen());
        buffer.putShort((short) acc.getClose());
        this.sendCmdWithBody(terminal, cmd, buffer.array());
    }

    public void generalParamSetting(String terminal, List<TerminalParam> params) {
        GeneralDto base = getVinAndSim(terminal);

        params.forEach(item -> {
            String value = item.getValue();
            byte[] body;
            GeneralParamEnum key = item.getKey();
            switch (key) {
                case HOST_IP:
                    String[] hosts = value.split(DOT);
                    byte[] bytes = new byte[key.getLen()];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = (byte) Integer.parseInt(hosts[i]);
                    }
                    body = key.getBytes(bytes);
                    break;

                case DEPUTY_IP:
                    String[] ips = value.split(DOT);
                    byte[] b = new byte[key.getLen()];
                    for (int i = 0; i < b.length; i++) {
                        b[i] = (byte) Integer.parseInt(ips[i]);
                    }
                    body = key.getBytes(b);
                    break;

                case PORT:
                    body = key.getBytes(Shorts.toByteArray((short) Integer.parseInt(value)));
                    break;

                case ACC_SUM_TIME:
                    body = key.getBytes(Ints.toByteArray((int) Long.parseLong(value)));
                    break;

                case IS_SLEEP:
                    byte[] sleep = {(byte) Integer.parseInt(value)};
                    body = key.getBytes(sleep);
                    break;

                case RESET:
                    body = key.getBytes(null);
                    break;
                case VIN_CODE:
                    byte[]  byte2 = value.getBytes(StandardCharsets.UTF_8);
                    if (byte2.length != VIN_CODE_LEN) {
                        throw new BadRequestAlertException("vin code  not correct", value, "vin.code.not.correct");
                    }
                    body = GeneralParamEnum.VIN_CODE.getBytes(byte2);
                    break;
                default:
                    throw new BadRequestException("general param is error");
            }

            int cmd = CmdConstant.GENERAL_PARAM_SET;
            String cmdBody = MessageUtil.packCmdBody(base.getSim(), Integer.parseInt(base.getProtocol()), cmd, getSerialNo(), body);
            this.sendCmdWithBody(terminal, cmd, cmdBody);
        });
    }

    public void generalParamQuery(String terminal, List<TerminalParam> params) {
        GeneralDto base = getVinAndSim(terminal);

        ByteBuffer buffer = ByteBuffer.allocate(params.size() * 2);
        params.forEach(item -> buffer.put(item.getKey().getBytes(null)));

        int cmd = CmdConstant.PARAM_QUERY;
        String cmdBody = MessageUtil.packCmdBody(base.getSim(), Integer.parseInt(base.getProtocol()), cmd, getSerialNo(), buffer.array());
        this.sendCmdWithBody(terminal, cmd, cmdBody);
    }

    /**
     * 根据终端编号获取 vin码，sim卡号，协议版本号
     *
     * @param terminalCode 终端编号
     * @return GeneralDto
     */
    public GeneralDto getVinAndSim(String terminalCode) {
        GeneralDto data = terminalDao.getGeneralDataByCode(terminalCode);
        if (data == null) {
            throw new BadRequestAlertException("not found valid terminal", terminalCode, "not.found.valid.terminal");
        }
        return data;
    }


    public void lock(CmdQuery param) {
        GeneralDto dto = terminalDao.getGeneralDataByVin(param.getVin());
        FunctionSetItemLock itemLock = functionSetItemLockDao.unique(param.getFunctionLockId());
        //a.发送锁车指令
        int version = Integer.parseInt(dto.getProtocol());
        DicItem dicItem = dicItemDao.createLambdaQuery().andEq(DicItem::getId, itemLock.getDicItemId()).single();
        int cmd = Integer.parseInt(dicItem.getItemValue(), 16);
        String cmdBody = MessageUtil.packCmdBody(dto.getSim(), version, cmd, getSerialNo(), itemLock.getMessage());
        this.sendCmdWithBodyLock(dto.getTerminalCode(), cmd, cmdBody, dicItem.getItemCode());
    }

    public List<TerminalTest> terminalTests(String terminalCode) {
        return testDao.terminalTests(terminalCode);
    }


    public void sendRetryCommand(String vin, int cmd, String reqBody) {
        GeneralDto base = terminalDao.getGeneralDataByVin(vin);

        LongConsumer consumer = commandId -> {
        };
        grampusCmdService.sendSendNoStore(base.getProtocolType(), vin, cmd, reqBody, consumer);
    }

    /**
     * 发送不带报文的指令
     *
     * @param terminalCode 终端编号
     * @param cmd          指令
     */
    private void sendNoReqBody(String terminalCode, int cmd, boolean isTest, String key) {
        GeneralDto base = getVinAndSim(terminalCode);
        LongConsumer consumer = isTest ? this.getTestConsumer(terminalCode, key) : commandId -> {
        };
        grampusCmdService.cmdSendOnline(base.getProtocolType(), base.getVin(), cmd,
                MessageUtil.packCmdBody(base.getSim(), Integer.parseInt(base.getProtocol()), cmd, getSerialNo(), (String) null),
                consumer);
    }

    /**
     * 发送带报文的指令
     *
     * @param terminalCode 终端编号
     * @param cmd          指令
     * @param body         报文字节
     */
    private void sendCmdWithBody(String terminalCode, Integer cmd, byte[] body) {
        Objects.requireNonNull(body, "body is null");
        GeneralDto base = getVinAndSim(terminalCode);

        LongConsumer consumer = commandId -> {
        };
        grampusCmdService.cmdSendOnline(base.getProtocolType(), base.getVin(), cmd,
                MessageUtil.packCmdBody(base.getSim(), Integer.parseInt(base.getProtocol()), cmd, getSerialNo(), body), consumer);
    }

    /**
     * 发送带报文的指令
     *
     * @param terminalCode 终端编号
     * @param cmd          指令
     * @param body         base64报文
     */
    private void sendCmdWithBody(String terminalCode, Integer cmd, String body) {
        Objects.requireNonNull(body, "body is null");
        GeneralDto base = getVinAndSim(terminalCode);

        LongConsumer consumer = commandId -> {
        };
        grampusCmdService.cmdSendOnline(base.getProtocolType(), base.getVin(), cmd, body, consumer);
    }


    /**
     * 发送带报文的指令
     *
     * @param terminalCode 终端编号
     * @param cmd          指令
     * @param body         base64报文
     */
    private void sendCmdWithBodyLock(String terminalCode, Integer cmd, String body, String itemCode) {
        Objects.requireNonNull(body, "body is null");
        GeneralDto base = getVinAndSim(terminalCode);

        LongConsumer consumer = this.getTestConsumer(terminalCode, itemCode);
        grampusCmdService.cmdSendOnline(base.getProtocolType(), base.getVin(), cmd, body, consumer);
    }

    public Long save(CommandDto dto) {
        return commandService.insert(dto);
    }

    public int getSerialNo() {
        return commandService.getSerialNo();
    }
}
