package cn.com.tiza.service;


import cn.com.tiza.dao.SoftVersionDao;
import cn.com.tiza.dao.TerminalDao;
import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.domain.Terminal;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.SoftVersionDto;
import cn.com.tiza.service.dto.SoftVersionQuery;
import cn.com.tiza.service.mapper.SoftVersionMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.web.error.ErrorKeyContant.SOFT_VERSION_CODE_HAS_USED;

/**
 * Service
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class SoftVersionService {

    @Autowired
    private SoftVersionDao softVersionDao;

    @Autowired
    private SoftVersionMapper softVersionMapper;

    @Autowired
    private TerminalDao terminalDao;

    public PageQuery<SoftVersion> findAll(SoftVersionQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        softVersionDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<SoftVersion> get(Long id) {
        return Optional.ofNullable(softVersionDao.single(id));
    }

    public SoftVersion create(SoftVersionDto command) {
        SoftVersion entity = softVersionMapper.dtoToEntity(command);
        softVersionDao.insert(entity);
        return entity;
    }

    public Optional<SoftVersion> update(Long id, SoftVersionDto command) {
        return get(id).map(entity -> {
            entity.setCode(command.getCode());
            entity.setCollectFunctionId(command.getCollectFunctionId());
            entity.setLockFunctionId(command.getLockFunctionId());
            entity.setName(command.getName());
            entity.setRemark(command.getRemark());
            softVersionDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        long count = terminalDao.createLambdaQuery().andEq(Terminal::getSoftVersionId, id).count();
        if (count > 0) {
            throw new BadRequestException("software.version.is.used.by.terminal");
        }
        softVersionDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<SoftVersion> getSoftVersions() {
        return softVersionDao.all();
    }

    public List<SelectOption> getVersions() {
        return getSoftVersions().stream()
                .map(sv -> new SelectOption(sv.getId(), sv.getCode(), sv.getName()))
                .collect(Collectors.toList());
    }

    public void checkUnique(String code) {
        long count = softVersionDao.createLambdaQuery()
                .andEq(SoftVersion::getCode, code)
                .count();
        if (count > 0) {
            throw new BadRequestAlertException("soft version code has been used", code, SOFT_VERSION_CODE_HAS_USED);
        }
    }

    public void checkUnique(Long id, String code) {
        SoftVersion sc = softVersionDao.createLambdaQuery()
                .andEq(SoftVersion::getCode, code)
                .single();
        if (sc != null && !Objects.equals(id, sc.getId())) {
            throw new BadRequestAlertException("soft version code has been used", code, SOFT_VERSION_CODE_HAS_USED);
        }
    }

}
