package cn.com.tiza.service;


import cn.com.tiza.dao.FunctionSetItemDao;
import cn.com.tiza.dao.SoftVersionDao;
import cn.com.tiza.domain.FunctionSetItem;
import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.service.dto.FunctionSetItemDto;
import cn.com.tiza.service.dto.FunctionSetItemQuery;
import cn.com.tiza.service.mapper.FunctionSetItemMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cn.com.tiza.web.error.ErrorKeyContant.FUNCTION_ITEM_CODE_USED;
import static cn.com.tiza.web.error.ErrorKeyContant.FUNCTION_SET_ITEM_USERD;

/**
 * Service
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class FunctionSetItemService {

    @Autowired
    private FunctionSetItemDao functionSetItemDao;

    @Autowired
    private FunctionSetItemMapper functionSetItemMapper;

    @Autowired
    private SoftVersionDao softVersionDao;

    @SuppressWarnings("all")
    public PageQuery<FunctionSetItem> findAll(FunctionSetItemQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        functionSetItemDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<FunctionSetItem> get(Long id) {
        return Optional.ofNullable(functionSetItemDao.single(id));
    }

    public FunctionSetItem create(FunctionSetItemDto command) {

        EntityValidator.checkUnique(this.getItemByCode(command.getFunctionId(), command.getCode()),
                null, FUNCTION_ITEM_CODE_USED);

        FunctionSetItem entity = functionSetItemMapper.dtoToEntity(command);
        functionSetItemDao.insert(entity);
        return entity;
    }

    private Optional<FunctionSetItem> getItemByCode(Long functionId, String code) {
        return Optional.ofNullable(functionSetItemDao.createLambdaQuery()
                .andEq(FunctionSetItem::getCode, code)
                .andEq(FunctionSetItem::getFunctionId, functionId)
                .single());
    }

    public Optional<FunctionSetItem> update(Long id, FunctionSetItemDto command) {

        EntityValidator.checkUnique(this.getItemByCode(command.getFunctionId(), command.getCode()),
                id, FUNCTION_ITEM_CODE_USED);

        return get(id).map(entity -> {
            entity.setBitLength(command.getBitLength());
            entity.setBitStart(command.getBitStart());
            entity.setCode(command.getCode());
            entity.setFunctionId(command.getFunctionId());
            entity.setName(command.getName());
            entity.setRate(command.getRate());
            entity.setUnit(command.getUnit());
            entity.setDescription(command.getDescription());
            entity.setVarAddress(command.getVarAddress());
            entity.setOffset(command.getOffset());
            entity.setSeparator(command.getSeparator());
            entity.setDataFormat(command.getDataFormat());
            entity.setSortNum(command.getSortNum());
            functionSetItemDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        long count = softVersionDao.createLambdaQuery()
                .orEq(SoftVersion::getCollectFunctionId, id)
                .orEq(SoftVersion::getLockFunctionId, id)
                .count();
        if(count > 0){
            throw new BadRequestAlertException("function set was used", null, FUNCTION_SET_ITEM_USERD);
        }
        functionSetItemDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
