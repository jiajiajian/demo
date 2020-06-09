package cn.com.tiza.service;


import cn.com.tiza.dao.DicItemDao;
import cn.com.tiza.dao.SimcardDao;
import cn.com.tiza.dao.TerminalDao;
import cn.com.tiza.domain.DicItem;
import cn.com.tiza.domain.Simcard;
import cn.com.tiza.domain.Terminal;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.dto.SimcardDto;
import cn.com.tiza.service.dto.SimcardQuery;
import cn.com.tiza.service.mapper.SimcardMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cn.com.tiza.web.error.ErrorKeyContant.SIMCARD_HAS_USED;

/**
 * Service
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class SimcardService {

    @Autowired
    private SimcardDao simcardDao;

    @Autowired
    private SimcardMapper simcardMapper;

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private DicItemDao dicItemDao;

    public static final String CARD_WAY = "CARD_WAY";
    public static final int SIMNumber = 11;
    public static final int SIMLongNumber = 13;


    public PageQuery<Simcard> findAll(SimcardQuery query) {
        PageQuery<Simcard> pageQuery = query.toPageQuery();
        simcardDao.pageQuery(pageQuery);
        pageQuery.getList().forEach(s -> s.setServiceState(this.parseSimStatus(s.getStatus())));
        return pageQuery;
    }

    public Optional<Simcard> get(Long id) {
        return Optional.ofNullable(simcardDao.single(id));
    }

    public Simcard create(SimcardDto command) {
        Simcard entity = simcardMapper.dtoToEntity(command);
        simcardDao.insert(entity);
        return entity;
    }

    public Optional<Simcard> update(Long id, SimcardDto command) {
        return get(id).map(entity -> {
            if(entity.getStatus() == 0){
                entity.setCode(command.getCode());
            }else {
                if(!Objects.equals(entity.getCode(), command.getCode())){
                    throw new BadRequestException("sim.code.not.update");
                }
            }
            entity.setCardWayId(command.getCardWayId());
            entity.setStatus(command.getStatus());
            entity.setCardOwner(command.getCardOwner());
            entity.setCreateTime(command.getCreateTime());
            entity.setOrderNo(command.getOrderNo());
            entity.setOperator(command.getOperator());
            entity.setDepartment(command.getDepartment());
            simcardDao.updateTemplateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        long count = terminalDao.createLambdaQuery().andEq(Terminal::getSimcardId, id).count();
        if(count > 0){
            throw new BadRequestException("sim.is.used.by.terminal");
        }
        this.get(id).ifPresent(sim->{
            //预销户不能被删除
            if(sim.getStatus() == 4){
                throw new BadRequestException("sim.status.is.ready.off");
            }
        });
        simcardDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public Simcard getSimcard(String code) {
        return simcardDao.createLambdaQuery()
                .andEq(Simcard::getCode, code)
                .single();
    }

    @SuppressWarnings("all")
    public List<DicItem> getOptionsByType(String code) {
        return dicItemDao.createLambdaQuery()
                .andEq(DicItem::getDicCode, code)
                .asc(DicItem::getSortCode)
                .select(DicItem::getId, DicItem::getItemCode, DicItem::getItemName, DicItem::getItemValue);
    }

    public void checkUnique(String code) {
        long count = simcardDao.createLambdaQuery()
                .andEq(Simcard::getCode, code)
                .count();
        if (count > 0) {
            throw new BadRequestAlertException("sim card has been used", code, SIMCARD_HAS_USED);
        }
    }

    public void checkUnique(Long id, String code) {
        Simcard sc = simcardDao.createLambdaQuery()
                .andEq(Simcard::getCode, code)
                .single();
        if (sc != null && !Objects.equals(id, sc.getId())) {
            throw new BadRequestAlertException("sim card has been used", code, SIMCARD_HAS_USED);
        }
    }

    public boolean resetCardStatusAndDeleteTerminal(Long terminalId, Integer cardStatus) {
        Terminal terminal = terminalDao.single(terminalId);
        if (Objects.isNull(terminal)) {
            return true;
        }
        Simcard simcard = simcardDao.single(terminal.getSimcardId());
        if (Objects.nonNull(simcard)) {
            simcard.setStatus(cardStatus);
            simcardDao.updateTemplateById(simcard);
        }
        terminalDao.deleteById(terminalId);
        return true;
    }

    public void abandon(Long id) {
        get(id).ifPresent(c -> {
            if(c.getStatus() != 4){
                throw new BadRequestException("sim.card.status.must.four");
            }
            //废弃
            c.setStatus(5);
            simcardDao.updateTemplateById(c);
        });
    }

    /**
     * 返回指定服务状态的所有sim 卡集合
     *
     * @param status 服务状态----0：未分配，1：已分配，2：暂停，3：到期，4：预销户，5：废弃
     * @return sim 卡集合
     */
    public List<Simcard> getSimcards(Integer status) {
        return simcardDao.createLambdaQuery()
                .andEq(Simcard::getStatus, status)
                .select();
    }

    public List<SimcardDto> readSimCards(ExcelReader<SimcardDto> reader, InputStream inputStream) throws IOException {

        List<SimcardDto> dtos = reader.create(inputStream).resolve();
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }

        Set<String> simSet = new HashSet<>(dtos.size());

        Set<String> simSetDb = Optional.ofNullable(simcardDao.all())
                .map(List::stream)
                .map(t -> t.map(Simcard::getCode).collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);

        Map<String, Integer> operatorMap = new HashMap<>();
        operatorMap.put("中国移动", 1);
        operatorMap.put("中国联通", 2);
        operatorMap.put("中国电信", 3);

        Map<String, Long> cardWayMap = getOptionsByType(CARD_WAY).stream().
                collect(Collectors.toMap(DicItem::getItemName, DicItem::getId));

        dtos.forEach(dto -> {
            String code = dto.getCode();
            if (code.length() != SIMNumber && code.length() != SIMLongNumber) {
                reader.addCellError(dto, 0, "SIM卡号格式错误");
            }

            if (simSet.contains(code)) {
                reader.addCellError(dto, 0, "SIM卡号重复");
            }

            if (simSetDb.contains(code)) {
                reader.addCellError(dto, 0, "该SIM卡号在数据库中已经存在");
            }

            simSet.add(code);

            if (StringUtils.isNotEmpty(dto.getOperatorName()) && operatorMap.get(dto.getOperatorName()) == null) {
                reader.addCellError(dto, 4, "该运营商不存在");
            }

            if (cardWayMap.get(dto.getCardWayName()) == null) {
                reader.addCellError(dto, 5, "该办卡方式不存在");
            }

            dto.setOperator(operatorMap.get(dto.getOperatorName()));
            dto.setCardWayId(cardWayMap.get(dto.getCardWayName()));
        });

        return dtos;
    }

    public void save(List<SimcardDto> simCardDtoList) {
        List<Simcard> list = simCardDtoList.stream().map(simcardMapper::dtoToEntity).collect(Collectors.toList());
        if (!list.isEmpty()) {
            simcardDao.insertBatch(list);
        }
    }

    /**
     * 0：未分配，1：已分配，2：暂停，3：到期，4：预销户，5：废弃
     *
     * @param state state
     * @return 状态
     */
    private String parseSimStatus(int state) {
        switch (state) {
            case 0:
                return "未分配";
            case 1:
                return "已分配";
            case 2:
                return "暂停";
            case 3:
                return "到期";
            case 4:
                return "预销户";
            case 5:
                return "废弃";

            default:
                throw new BadRequestException("错误的状态值：" + state);
        }
    }
}
