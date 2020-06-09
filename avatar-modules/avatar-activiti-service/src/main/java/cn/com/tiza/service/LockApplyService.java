package cn.com.tiza.service;


import cn.com.tiza.constant.ApplyStateEnum;
import cn.com.tiza.dao.LockApplyDao;
import cn.com.tiza.domain.LockApply;
import cn.com.tiza.service.dto.LockApplyDto;
import cn.com.tiza.service.dto.LockApplyQuery;
import cn.com.tiza.service.mapper.LockApplyMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static cn.com.tiza.constant.ProcDefKey.CONTROLLER_LOCK;

/**
 * Service
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class LockApplyService {

    @Autowired
    private LockApplyDao lockApplyDao;

    @Autowired
    private LockApplyMapper lockApplyMapper;

    @Autowired
    private LockApplyVinService vinService;

    @Autowired
    private MyTaskService myTaskService;

    @SuppressWarnings("unchecked")
    public PageQuery<LockApply> findAll(LockApplyQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        lockApplyDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<LockApply> get(Long id) {
        return Optional.ofNullable(lockApplyDao.single(id));
    }

    public LockApply getLockApply(String  instanceId){
        return lockApplyDao.createLambdaQuery().andEq(LockApply::getInstanceId,instanceId).single();
    }

    public LockApply create(LockApplyDto command) {
        LockApply entity = lockApplyMapper.dtoToEntity(command);
        lockApplyDao.insert(entity, true);
        Objects.requireNonNull(entity.getId(), "申请单号ID不能为空！");
        vinService.saveBatch(command.getVinList(), entity.getId());
        //启动流程
        String instanceId = myTaskService.startProcess(CONTROLLER_LOCK, entity.getApplyCode(), null, null,
                ints -> lockApplyDao.updateInstanceId(entity.getApplyCode(), ints.getId()));
        entity.setInstanceId(instanceId);
        return entity;
    }

    public Optional<LockApply> update(Long id, LockApplyDto command) {
        return get(id).map(entity -> {
            this.checkCanOperate(entity);
            entity.setReason(command.getReason());
            //修改审批单号中关联的车
            vinService.delete(id);
            vinService.saveBatch(command.getVinList(), id);
            if (Objects.equals(entity.getState(), ApplyStateEnum.fail.getState())){
                //之前状态为未通过，将状态修改为待审批，重新启动流程
                entity.setState(ApplyStateEnum.no_approved.getState());
                myTaskService.startProcess(CONTROLLER_LOCK, entity.getApplyCode(), null, null,
                        ints -> lockApplyDao.updateInstanceId(entity.getApplyCode(), ints.getId()));
            }
            lockApplyDao.updateTemplateById(entity);
            return entity;
        });
    }

    private void checkCanOperate(LockApply entity) {
        Integer state = entity.getState();
        if (Objects.equals(state, ApplyStateEnum.success.getState()) ||
                Objects.equals(state, ApplyStateEnum.approving.getState())) {
            throw new BadRequestAlertException("applying and applied is not edit", entity.getReason(),
                    "applying.applied.is.not.edit");
        }
    }

    public void delete(Long id) {
        get(id).ifPresent(entity -> {
            this.checkCanOperate(entity);
            lockApplyDao.deleteById(id);
        });
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
