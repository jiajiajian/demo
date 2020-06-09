package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.DicItemDao;
import cn.com.tiza.dao.TerminalDao;
import cn.com.tiza.domain.DicItem;
import cn.com.tiza.domain.Simcard;
import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.domain.Terminal;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.dto.GeneralDto;
import cn.com.tiza.service.dto.SimcardDto;
import cn.com.tiza.service.dto.TerminalDto;
import cn.com.tiza.service.dto.TerminalQuery;
import cn.com.tiza.service.mapper.TerminalMapper;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.rest.VehicleClient;
import cn.com.tiza.web.rest.dto.VehicleDto;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cn.com.tiza.web.error.ErrorKeyContant.*;

/**
 * Service
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TerminalService {

    private static final String T_PROTOCOL_TYPE = "T_PROTOCOL_TYPE";

    private static final String TERMINAL_MODEL = "TERMINAL_MODEL";

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private DicItemDao dicItemDao;

    @Autowired
    private SimcardService simcardService;

    @Autowired
    private SoftVersionService softVersionService;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private AccountApiClient accountApiClient;

    @Autowired
    private VehicleClient vehicleClient;

    public PageQuery<Terminal> findAll(TerminalQuery query) {
        if (query.getOrganizationId() == null) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = query.toPageQuery();
        pageQuery.setOrderBy("t.id desc");
        terminalDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<Terminal> get(Long id) {
        return Optional.ofNullable(terminalDao.single(id));
    }

    public TerminalDto getOfUpdate(Long id) {
        return terminalDao.getOfUpdate(id);
    }

    public Terminal create(TerminalDto command) {
        String simCode = command.getSimCode();
        Simcard simcard = simcardService.getSimcard(simCode);
        if (simcard == null) {
            throw new BadRequestAlertException("sim code is not exist", simCode, SIMCARD_IS_NOT_EXISTED);
        }
        if (simcard.getStatus() != 0 && simcard.getStatus() != 4) {
            throw new BadRequestAlertException("sim card has been used", simCode, SIM_CARD_NOT_ZERO_OR_FOUR);
        }

        Terminal entity = terminalMapper.dtoToEntity(command);
        entity.setSimcardId(simcard.getId());
        terminalDao.insert(entity, true);

        this.updateSimState(simcard.getId(), 1);

        //向车辆表中插入数据
        command.setId(entity.getId());
        VehicleDto dto = terminalMapper.toVehicleDto(command);
        vehicleClient.create(dto);
        return entity;
    }


    public Optional<Terminal> update(Long id, TerminalDto command) {
        String simCode = command.getSimCode();
        Simcard simcard = simcardService.getSimcard(simCode);
        if (simcard == null) {
            throw new BadRequestAlertException("sim code is not exist", simCode, SIMCARD_IS_NOT_EXISTED);
        }
        return get(id).map(entity -> {
            entity.setProduceDate(command.getProduceDate());
            entity.setFirmWireVersion(command.getFirmWireVersion());
            entity.setProtocolId(command.getProtocolId());
            Long oldSimId = entity.getSimcardId();
            Long newId = simcard.getId();
            if (!Objects.equals(newId, oldSimId)) {
                if (simcard.getStatus() != 0 && simcard.getStatus() != 4) {
                    throw new BadRequestAlertException("sim card has been used", simCode, SIM_CARD_NOT_ZERO_OR_FOUR);
                }
                //原来得变为未分配
                this.updateSimState(oldSimId, 0);
                //新得变为已分配
                this.updateSimState(newId, 1);
                entity.setSimcardId(newId);
            }
            entity.setSoftVersionId(command.getSoftVersionId());
            entity.setTerminalModel(command.getTerminalModel());
            terminalDao.updateTemplateById(entity);
            return entity;
        });
    }


    public void delete(Long id) {
        Optional.ofNullable(getOfUpdate(id)).ifPresent(t -> {
            if (t.getVin() != null) {
                throw new BadRequestException("terminal.is.used.by.vin");
            }
            this.updateSimState(t.getSimcardId(), 0);
            terminalDao.deleteById(id);
        });
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @SuppressWarnings("all")
    public List<DicItem> getOptionsByType(String code) {
        return dicItemDao.createLambdaQuery()
                .andEq(DicItem::getDicCode, code)
                .asc(DicItem::getSortCode)
                .select(DicItem::getId, DicItem::getItemCode, DicItem::getItemName, DicItem::getItemValue);
    }

    @SuppressWarnings("unchecked")
    public List<DicItem> getOptionsByTypes(List<String> codes) {
        if (codes.isEmpty()) {
            return Lists.newArrayList();
        }
        return dicItemDao.createLambdaQuery()
                .andIn(DicItem::getDicCode, codes)
                .asc(DicItem::getSortCode)
                .select(DicItem::getId, DicItem::getItemCode, DicItem::getItemName, DicItem::getItemValue);
    }

    public void checkUnique(String code) {
        long count = terminalDao.createLambdaQuery()
                .andEq(Terminal::getCode, code)
                .count();
        if (count > 0) {
            throw new BadRequestAlertException("terminal code has been used", code, TERMINAL_HAS_USED);
        }
    }

    public void checkUnique(Long id, String code) {
        Terminal terminal = terminalDao.createLambdaQuery()
                .andEq(Terminal::getCode, code)
                .single();
        if (terminal != null && !Objects.equals(id, terminal.getId())) {
            throw new BadRequestAlertException("terminal code has been used", code, TERMINAL_HAS_USED);
        }
    }

    public List<TerminalDto> readTerminal(ExcelReader<TerminalDto> reader, InputStream in) throws IOException {
        List<TerminalDto> dtos = reader.create(in).resolve();
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }

        Set<String> terSet = new HashSet<>(dtos.size());
        Set<String> simSet = new HashSet<>(dtos.size());

        Set<String> terSetDb = Optional.ofNullable(terminalDao.all())
                .map(List::stream)
                .map(t -> t.map(Terminal::getCode).collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);

        Map<String, Long> simMap = Optional.ofNullable(simcardService.getSimcards(0))
                .map(List::stream)
                .map(s -> s.collect(Collectors.toMap(Simcard::getCode, Simcard::getId)))
                .orElseGet(Collections::emptyMap);
        Set<String> simSetDb = simMap.keySet();

        Map<String, Long> versionMap = Optional.ofNullable(softVersionService.getSoftVersions())
                .map(List::stream)
                .map(v -> v.collect(Collectors.toMap(SoftVersion::getName, SoftVersion::getId)))
                .orElseGet(Collections::emptyMap);

        Map<String, Long> protocolMap = getOptionsByType(T_PROTOCOL_TYPE).stream()
                .collect(Collectors.toMap(DicItem::getItemName, DicItem::getId));

        Set<String> terModelSet = getOptionsByType(TERMINAL_MODEL).stream()
                .map(DicItem::getItemCode).collect(Collectors.toSet());

        Map<String, Long> orgMap = Optional.ofNullable(accountApiClient.getChildrenOrgs())
                .map(org -> org.stream().collect(Collectors.toMap(SelectOption::getName, SelectOption::getId)))
                .orElseGet(Collections::emptyMap);

        dtos.forEach(dto -> {
            String code = dto.getCode();
            String simCode = dto.getSimCode();
            if (terSet.contains(code)) {
                reader.addCellError(dto, 0, "终端编号重复");
            }

            if (terSetDb.contains(code)) {
                reader.addCellError(dto, 0, "该终端编号在数据库中已经存在");
            }

            if (simSet.contains(simCode)) {
                reader.addCellError(dto, 1, "SIM卡号重复");
            }

            if (!simSetDb.contains(simCode)) {
                reader.addCellError(dto, 1, "该SIM卡号不存在");
            }

            if (versionMap.get(dto.getSoftwareVersion()) == null) {
                reader.addCellError(dto, 2, "该软件版本不存在");
            }

            if (protocolMap.get(dto.getProtocolName()) == null) {
                reader.addCellError(dto, 3, "该通信协议不存在");
            }

            if (!terModelSet.contains(dto.getTerminalModel())) {
                reader.addCellError(dto, 5, "该终端型号不存在");
            }

            if (orgMap.get(dto.getOrgName()) == null) {
                reader.addCellError(dto, 6, "该机构名称不存在");
            }

            terSet.add(code);
            simSet.add(simCode);
            dto.setSimcardId(simMap.get(simCode));
            dto.setSoftVersionId(versionMap.get(dto.getSoftwareVersion()));
            dto.setProtocolId(protocolMap.get(dto.getProtocolName()));
            dto.setOrganizationId(orgMap.get(dto.getOrgName()));
        });

        return dtos;
    }

    public void save(List<TerminalDto> dtoList) {
        dtoList.forEach(this::create);
    }

    private void updateSimState(Long id, Integer state) {
        //修改sim卡得状态
        SimcardDto simDto = new SimcardDto();
        simDto.setStatus(state);
        simcardService.update(id, simDto);
    }

    /**
     * get the protocol type of terminal
     *
     * @param keyword terminal code or vin or sim code
     * @return protocol type
     */
    public String getProtocolType(String keyword) {
        GeneralDto dto = terminalDao.getGeneralDataByKeyword(keyword);
        Objects.requireNonNull(dto, dto + "which gets protocol type can not be null");
        return dto.getProtocolType();
    }
}
