package cn.com.tiza.service;


import cn.com.tiza.dao.FunctionSetDao;
import cn.com.tiza.dao.SoftVersionDao;
import cn.com.tiza.domain.FunctionSet;
import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.FunctionSetDto;
import cn.com.tiza.service.dto.FunctionSetQuery;
import cn.com.tiza.service.mapper.FunctionSetMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class FunctionSetService {

    @Autowired
    private FunctionSetDao functionSetDao;

    @Autowired
    private FunctionSetMapper functionSetMapper;

    @Autowired
    private SoftVersionDao softVersionDao;

    @SuppressWarnings("unchecked")
    public PageQuery<FunctionSet> findAll(FunctionSetQuery query) {
        PageQuery<FunctionSet> pageQuery = query.toPageQuery();
        functionSetDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<FunctionSet> get(Long id) {
        return Optional.ofNullable(functionSetDao.single(id));
    }

    public FunctionSet create(FunctionSetDto command) {
        FunctionSet entity = functionSetMapper.dtoToEntity(command);
        functionSetDao.insert(entity);
        return entity;
    }

    public Optional<FunctionSet> update(Long id, FunctionSetDto command) {
        return get(id).map(entity -> {
            entity.setCode(command.getCode());
            entity.setFunctionType(command.getFunctionType());
            entity.setName(command.getName());
            entity.setRemark(command.getRemark());
            functionSetDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        long count = softVersionDao.createLambdaQuery()
                .orEq(SoftVersion::getCollectFunctionId, id)
                .orEq(SoftVersion::getLockFunctionId, id).count();
        if (count > 0) {
            throw new BadRequestException("function.set.is.used.by.software");
        }
        functionSetDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectOption> getFunctionSets(Integer type) {
        return functionSetDao.createLambdaQuery()
                .andEq(FunctionSet::getFunctionType, type)
                .select(FunctionSet::getId, FunctionSet::getCode, FunctionSet::getName)
                .stream()
                .map(set -> new SelectOption(set.getId(), set.getCode(), set.getName()))
                .collect(Collectors.toList());

    }
}
