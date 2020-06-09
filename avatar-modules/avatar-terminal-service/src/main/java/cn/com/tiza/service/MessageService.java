package cn.com.tiza.service;

import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dao.DicItemDao;
import cn.com.tiza.dao.TerminalDao;
import cn.com.tiza.domain.DicItem;
import cn.com.tiza.dto.RestDataRecord;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.dto.GeneralDto;
import cn.com.tiza.service.dto.MessageQuery;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.vm.MessageVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author villas
 */
@Slf4j
@Service
public class MessageService {

    private static final String FIRST = "0";

    private static final String MSG_FORMAT = "CMD_ID";

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private GrampusDataQueryService dataQueryService;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private DicItemDao dicItemDao;

    public List<MessageVM> query(MessageQuery query) {
        Map<String,String> dicItemMap = getOptionsByCode().stream().collect(Collectors.toMap(DicItem::getItemCode,DicItem::getItemName));
        GeneralDto dto = this.getGeneralData(query.getKeyword());
        if (dto == null) {
            throw new BadRequestAlertException("not found valid terminal", query.getKeyword(), "not.found.valid.terminal");
        }

        String terminalType = dto.getProtocolType();

        int cmd = query.getCmd() == null ? 0 : query.getCmd();

        List<MessageVM> result = new ArrayList<>(1000);

        dataQueryService.queryRawData(terminalType, dto.getVin(), query.getStart(), query.getEnd(), cmd, false,
                record -> this.assemble(record, dto,dicItemMap), result::add);
        return result;
    }

    public List<MessageVM> exportData(MessageQuery query) {
        Map<String,String> dicItemMap = getOptionsByCode().stream().collect(Collectors.toMap(DicItem::getItemCode,DicItem::getItemName));

        GeneralDto dto = this.getGeneralData(query.getKeyword());

        String terminalType = dto.getProtocolType();

        int cmd = query.getCmd() == null ? 0 : query.getCmd();

        List<MessageVM> result = new LinkedList<>();

        dataQueryService.queryRawData(terminalType, dto.getVin(), query.getStart(), query.getEnd(), cmd, true,
                record -> this.assemble(record, dto,dicItemMap), result::add);
        return result;
    }


    private GeneralDto getGeneralData(String keyword) {
        return terminalDao.getGeneralDataByKeyword(keyword);
    }

    private MessageVM assemble(RestDataRecord record, GeneralDto dto,Map<String,String> dicItemMap) {
        MessageVM vm = new MessageVM();
        vm.setVin(dto.getVin());
        vm.setSim(dto.getSim());
        vm.setTerminalCode(dto.getTerminalCode());
        vm.setBodyLength(record.getBody().length());
        vm.setProtocolType(this.protocolType(dto.getProtocol()));
        vm.setCmdType(dicItemMap.get(String.valueOf(record.getCmdID())));
        vm.setBody(record.getBody());
        vm.setCollectTime(ExcelWriter.timeConvert(record.getTime()));
        return vm;
    }

    private String protocolType(String protocol) {
        if (Objects.equals(protocol, FIRST)) {
            return "工程机械1.0协议";
        } else {
            return "工程机械1.1协议";
        }
    }


    private List<DicItem> getOptionsByCode() {
        return dicItemDao.createLambdaQuery()
                .andEq(DicItem::getDicCode, MSG_FORMAT)
                .asc(DicItem::getSortCode)
                .select(DicItem::getId, DicItem::getItemCode, DicItem::getItemName, DicItem::getItemValue);
    }
}
