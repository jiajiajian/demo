package cn.com.tiza.service;


import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.TlaDao;
import cn.com.tiza.domain.Tla;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.DTCEffectQuery;
import cn.com.tiza.service.dto.TlaDto;
import cn.com.tiza.service.dto.TlaQuery;
import cn.com.tiza.service.mapper.TlaMapper;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.DTCEffectVm;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.util.EntityValidator.checkUnique;

/**
 * Service
 * gen by beetlsql 2020-05-12
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class TlaService {

    @Autowired
    private TlaDao tlaDao;

    @Autowired
    private TlaMapper tlaMapper;

    @Autowired
    private AlarmHistoryDao historyDao;

    public PageQuery<Tla> findAll(TlaQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        tlaDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<Tla> get(Long id) {
        return Optional.ofNullable(tlaDao.single(id));
    }

    public Tla create(TlaDto command) {
        //checkNameExist(null, command.getOrganizationId(), command.getTla());
        checkTlaIdExist(null, command.getOrganizationId(), command.getTlaId());
        Tla entity = tlaMapper.dtoToEntity(command);
        tlaDao.insert(entity);
        return entity;
    }

    private void checkNameExist(Long id, Long orgId, String name) {
        Tla tla = tlaDao.findByNameAndOrg(name, orgId);
        checkUnique(Optional.ofNullable(tla), id, ErrorConstants.TLA_NAME_EXIST);

    }

    private void checkTlaIdExist(Long id, Long orgId, String tlaId) {

        Tla tla1 = tlaDao.findByTlaIdAndOrg(tlaId, orgId);
        checkUnique(Optional.ofNullable(tla1), id, ErrorConstants.TLA_ID_EXIST);
    }

    public Optional<Tla> update(Long id, TlaDto command) {
        return get(id).map(entity -> {
            //checkNameExist(id, command.getOrganizationId(), command.getTla());
            checkTlaIdExist(id, command.getOrganizationId(), command.getTlaId());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setTla(command.getTla());
            entity.setTlaId(command.getTlaId());
            tlaDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        tlaDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public PageQuery<DTCEffectVm> dtcAffectList(DTCEffectQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        historyDao.dTCEffectStatistic(pageQuery);
        return pageQuery;
    }

    public List<SelectOption> tlaOptions(Long rootOrgId) {
        return tlaDao.createLambdaQuery()
                .andEq(Tla::getOrganizationId, rootOrgId)
                .select()
                .stream()
                .map(tla -> {
                    SelectOption option = new SelectOption();
                    option.setName(tla.getTla());
                    option.setId(tla.getId());
                    option.setCode(tla.getTlaId());
                    return option;
                }).collect(Collectors.toList());
    }
}
