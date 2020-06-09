package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.*;
import cn.com.tiza.domain.DicItem;
import cn.com.tiza.domain.FunctionSetItemLock;
import cn.com.tiza.domain.Lock;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.util.MessageUtil;
import cn.com.tiza.web.rest.dto.VehicleRealtime;
import cn.com.tiza.web.rest.dto.VehicleVM;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static cn.com.tiza.web.error.ErrorKeyContant.CMD_CANNOT_BE_SENT;

/**
 * Service
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class LockService {

    @Autowired
    private LockDao lockDao;

    @Autowired
    private GrampusCmdService grampusCmdService;


    @Autowired
    private VehicleRealtimeDao vehicleRealtimeDao;

    @Autowired
    private FunctionSetItemLockDao functionSetItemLockDao;


    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private DicItemDao dicItemDao;

    @Autowired
    private RemoteLockCarLogDao lockCarLogDao;

    @Autowired
    private CmdService cmdService;


    @SuppressWarnings("unchecked")
    public PageQuery<Lock> findAll(LockQuery query) {
        if (query.getOrgId() == null) {
            query.setOrgId(BaseContextHandler.getOrgId());
        }
        PageQuery<Lock> pageQuery = query.toPageQuery();
        lockDao.pageQuery(pageQuery);
        this.setLockStatus(pageQuery.getList());
        return pageQuery;
    }


    private void setLockStatus(List<Lock> locks) {
        if (ListUtil.isEmpty(locks)) {
            return;
        }
        List<String> ters = locks.stream().map(Lock::getTerminalCode).collect(Collectors.toList());
        List<Lock> lockFunctionSet = lockDao.getLockFunctionSet(ters);
        Map<String, List<Lock>> map = lockFunctionSet.stream()
                .collect(Collectors.groupingBy(Lock::getTerminalCode));
        locks.forEach(t -> {
            HashMap<String, Integer> lockStatus = Maps.newHashMap();
            Set<String> collect = map.get(t.getTerminalCode()).stream().map(Lock::getItemCode).collect(Collectors.toSet());
            if (collect.contains(LockEnum.CONTROLLER_LOCK.name())) {
                lockStatus.put(LockEnum.CONTROLLER_LOCK.name(), null == t.getLock() ? 0 : t.getLock());
            }
            if (collect.contains(LockEnum.RELAY_LOCK_1.name())) {
                lockStatus.put(LockEnum.RELAY_LOCK_1.name(), null == t.getOneLevelLock() ? 0 : t.getOneLevelLock());
            }
            if (collect.contains(LockEnum.RELAY_LOCK_2.name())) {
                lockStatus.put(LockEnum.RELAY_LOCK_2.name(), null == t.getTwoLevelLock() ? 0 : t.getTwoLevelLock());
            }
            if (collect.contains(LockEnum.RELAY_LOCK_3.name())) {
                lockStatus.put(LockEnum.RELAY_LOCK_3.name(), null == t.getThreeLevelLock() ? 0 : t.getThreeLevelLock());
            }
            t.setLockStatus(lockStatus);
        });
    }


    public PageQuery<Lock> findAllLogs(RemoteLockCarLogQuery query) {
        if (query.getOrganizationId() == null) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<Lock> pageQuery = query.toPageQuery();
        lockCarLogDao.pageQuery(pageQuery);
        return pageQuery;
    }


    public PageQuery<VehicleVM> getVehicleList(LockQuery query) {
        if (query.getOrgId() == null) {
            query.setOrgId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = query.toPageQuery();
        lockDao.getVehicleList(pageQuery);
        return pageQuery;
    }

    public Optional<Lock> get(Long id) {
        return Optional.ofNullable(lockDao.single(id));
    }

    public void create(String[] vinArray) {
        List<Lock> locks = Arrays.stream(vinArray).map(Lock::new).collect(Collectors.toList());
        lockDao.insertBatch(locks);
    }

    public void delete(Long id) {
        lockDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public void cmdSend(List<CmdQuery> params) {
        for (CmdQuery param : params) {
            //a.判断是否可以发送指令
            GeneralDto dto = terminalDao.getGeneralDataByVin(param.getVin());
            if (!dto.isCanSendCmd()) {
                throw new BadRequestAlertException("command.cannot.be.sent", dto.getRunState() + "", CMD_CANNOT_BE_SENT);
            }
            //b.获取功锁车能集
            FunctionSetItemLock itemLock = functionSetItemLockDao.unique(param.getFunctionLockId());

            //每次发锁车指令把对应的指令id回写回业务库
            Lock lock = lockDao.createLambdaQuery()
                    .andEq(Lock::getVin, param.getVin())
                    .andEq(Lock::getFlag, 0)
                    .single();
            if (Objects.isNull(lock)) {
                continue;
            }
            LongConsumer consumer = commandId -> {
                lock.setCommandId(commandId);
                lock.setDicItemId(itemLock.getDicItemId());
                lockDao.updateById(lock);
                //新增一条记录用作日志记录
                lock.setFlag(1);
                lockDao.insert(lock);
            };

            //c.判断在线状态发送实时指令或离线指令
            int cmd = getCmdId(itemLock.getDicItemId());
            int version = Integer.parseInt(dto.getProtocol());
            String cmdBody = MessageUtil.packCmdBody(dto.getSim(), version, cmd, cmdService.getSerialNo(), itemLock.getMessage());

            if (isOnline(param.getVin())) {
                grampusCmdService.cmdSendOnline(dto.getProtocolType(), param.getVin(), cmd, cmdBody, consumer);
            } else {
                grampusCmdService.cmdSendOffline(dto.getProtocolType(), param.getVin(), cmd, cmdBody, consumer);
            }

        }
    }

    public int getCmdId(Long itemId) {
        String command = dicItemDao.createLambdaQuery().andEq(DicItem::getId, itemId).single().getItemValue();
        return Integer.parseInt(command, 16);
    }


    /**
     * 判断车辆是否在线
     *
     * @param vin vin码
     * @return true 在线， false 不在线
     */
    public boolean isOnline(String vin) {

        Long updateTime = vehicleRealtimeDao.createLambdaQuery().andEq(VehicleRealtime::getVin, vin)
                .single()
                .getDataUpdateTime();

        return isOnline(updateTime);
    }

    public static boolean isOnline(Long time) {
        if (time == null) {
            return false;
        }
        long flag = System.currentTimeMillis() - 300000;
        return time > flag;
    }

}
