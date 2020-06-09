package cn.com.tiza.service;


import cn.com.tiza.dao.FunctionSetItemLockDao;
import cn.com.tiza.domain.FunctionSetItemLock;
import cn.com.tiza.service.dto.FunctionSetItemLockDto;
import cn.com.tiza.service.dto.FunctionSetItemLockQuery;
import cn.com.tiza.service.mapper.FunctionSetItemLockMapper;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class FunctionSetItemLockService {

    @Autowired
    private FunctionSetItemLockDao functionSetItemLockDao;

    @Autowired
    private FunctionSetItemLockMapper functionSetItemLockMapper;

    public PageQuery<FunctionSetItemLock> findAll(FunctionSetItemLockQuery query) {
        PageQuery<FunctionSetItemLock> pageQuery = query.toPageQuery();
        functionSetItemLockDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<FunctionSetItemLock> get(Long id) {
        return Optional.ofNullable(functionSetItemLockDao.single(id));
    }

    public FunctionSetItemLock create(FunctionSetItemLockDto command) {
        FunctionSetItemLock entity = functionSetItemLockMapper.dtoToEntity(command);
        functionSetItemLockDao.insert(entity);
        return entity;
    }

    public Optional<FunctionSetItemLock> update(Long id, FunctionSetItemLockDto command) {
        return get(id).map(entity -> {
            entity.setChinaName(command.getChinaName());
            entity.setCode(command.getCode());
            entity.setDicItemId(command.getDicItemId());
            entity.setEnglishName(command.getEnglishName());
            entity.setMessage(command.getMessage());
            functionSetItemLockDao.upsertByTemplate(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        functionSetItemLockDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<FunctionSetItemLock> getLockOptions(String terminalCode) {
        return functionSetItemLockDao.getLockOptions(terminalCode);
    }
}
