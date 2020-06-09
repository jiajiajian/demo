package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.ChargeDao;
import cn.com.tiza.dao.ChargeDetailDao;
import cn.com.tiza.domain.Charge;
import cn.com.tiza.domain.ChargeDetail;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.ChargeDetailMapper;
import cn.com.tiza.service.mapper.ChargeMapper;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.collection.ListUtil;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ChargeService {
    private static final String KEY = "CHARGE_CONFIG_";
    private static final String FEE = "fee";
    private static final String MON_COUNT = "monCount";
    private static final String VEHICLE_EXPIRE_NUM = "VEHICLE_EXPIRE_NUM_";
    private static final String VEHICLE_EXPIRE_CLOSE_COUNT = "VEHICLE_EXPIRE_CLOSE_COUNT_";


    @Autowired
    private ChargeDao chargeDao;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private ChargeDetailDao chargeDetailDao;

    @Autowired
    private ChargeDetailMapper chargeDetailMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public PageQuery<ChargeVM> findAll(ChargeQuery query) {
        PageQuery<ChargeVM> pageQuery = query.toPageQuery();
        chargeDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<Charge> get(Long id) {
        return Optional.ofNullable(chargeDao.single(id));
    }

    public Charge create(ChargeDto command) {
        Charge entity = chargeMapper.dtoToEntity(command);
        //检查该组织下 是否已配置该终端型号
        Charge charge = chargeDao.findByOrgAndTerminalModel(entity.getOrganizationId(), entity.getTerminalModel());
        if (Objects.nonNull(charge)) {
            throw new BadRequestException(ErrorConstants.CHARGE_HAS_CREATED);
        }
        chargeDao.insert(entity);
        redisTemplate.opsForHash().put(KEY + entity.getOrganizationId(), entity.getTerminalModel(), "");
        return entity;
    }

    public ChargeConfigVM getChargeConfig(Long id) {
        return Optional.ofNullable(chargeDao.single(id))
                .map(charge -> {
                    ChargeConfigVM config = new ChargeConfigVM();
                    config.setId(charge.getId());
                    config.setTerminalModel(charge.getTerminalModel());
                    config.setChargeDetailList(chargeDetailMapper.entitiesToVMList(
                            chargeDetailDao.createLambdaQuery()
                                    .andEq(ChargeDetail::getChargeId, id)
                                    .select()
                    ));
                    return config;
                })
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ENTITY_NOT_FOUND_TYPE));
    }


    public void addConfigDetail(Long chargeId, ChargeDetailDto dto) {
        Charge charge = chargeDao.single(chargeId);
        if (Objects.isNull(charge)) {
            return;
        }
        List<ChargeDetail> chargeDetails = chargeDetailDao.createLambdaQuery().andEq(ChargeDetail::getChargeId, charge.getId()).select();
        ChargeDetail entity = chargeDetailMapper.dtoToEntity(dto, chargeId);
        if (chargeDetails.isEmpty()) {
            chargeDetailDao.insert(entity);
        } else {
            chargeDetails.forEach(detail -> {
                boolean isBetween = isBetween(dto, detail);
                if (isBetween) {
                    throw new BadRequestException(ErrorConstants.CHARGE_DETAIL_DATE_DOUBLICATION);
                }
            });
            chargeDetailDao.insert(entity);
        }
        chargeDetails.add(entity);
        //更新redis对应配置
        redisTemplate.opsForHash().put(KEY + charge.getOrganizationId(), charge.getTerminalModel(), JSON.toJSONString(chargeDetails));
        charge.setConfigFlag(1);
        chargeDao.updateById(charge);
    }

    public void updateConfigDetail(Long chargeDetailId, ChargeDetailDto dto) {
        ChargeDetail chargeDetail = chargeDetailDao.single(chargeDetailId);
        if (Objects.isNull(chargeDetail)) {
            return;
        }
        Charge charge = chargeDao.createLambdaQuery().andEq(Charge::getId, chargeDetail.getChargeId()).single();
        if (Objects.isNull(charge)) {
            return;
        }
        List<ChargeDetail> chargeDetails = chargeDetailDao.createLambdaQuery().andEq(ChargeDetail::getChargeId, charge.getId()).select();
        if (chargeDetails.isEmpty()) {
            return;
        }
        ChargeDetail entity = chargeDetailMapper.dtoToEntity(dto, charge.getId());
        entity.setId(chargeDetailId);
        for (int i = 0; i < chargeDetails.size(); i++) {
            if (chargeDetails.get(i).getId().equals(chargeDetailId)) {
                chargeDetails.remove(i);
                break;
            }
        }
        if (chargeDetails.size() == 0) {
            chargeDetailDao.updateById(entity);
        } else {
            chargeDetails.forEach(detail -> {
                boolean isBetween = isBetween(dto, detail);
                if (isBetween) {
                    throw new BadRequestException(ErrorConstants.CHARGE_DETAIL_DATE_DOUBLICATION);
                }
            });

            chargeDetailDao.updateById(entity);
        }

        chargeDetails.add(entity);
        //更新redis对应配置
        redisTemplate.opsForHash().put(KEY + charge.getOrganizationId(), charge.getTerminalModel(), JSON.toJSONString(chargeDetails));
    }

    /**
     * 判断yyyyMM 是否包含在某个时间区间没
     */
    public boolean isBetween(ChargeDetailDto dto, ChargeDetail detail) {
        if (Objects.isNull(detail.getEnd())) {
            if (Objects.isNull(dto.getEnd())) {
                return true;
            } else if (dto.getEnd() >= detail.getBegin()) {
                return true;
            }
        } else {
            if (Objects.isNull(dto.getEnd())) {
                if (dto.getBegin() <= detail.getEnd()) {
                    return true;
                }
            } else {
                if ((dto.getBegin() >= detail.getBegin() && dto.getBegin() <= detail.getEnd()) ||
                        (dto.getEnd() >= detail.getBegin() && dto.getEnd() <= detail.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteConfigDetail(Long[] ids) {
        if (ids.length == 0) {
            return;
        }
        for (int j = 0; j < ids.length; j++) {
            Long chargeDetailId = ids[j];
            ChargeDetail chargeDetail = chargeDetailDao.single(chargeDetailId);
            if (Objects.isNull(chargeDetail)) {
                return;
            }
            Charge charge = chargeDao.createLambdaQuery().andEq(Charge::getId, chargeDetail.getChargeId()).single();
            if (Objects.isNull(charge)) {
                return;
            }
            List<ChargeDetail> chargeDetails = chargeDetailDao.createLambdaQuery().andEq(ChargeDetail::getChargeId, charge.getId()).select();
            if (chargeDetails.isEmpty()) {
                return;
            }
            for (int i = 0; i < chargeDetails.size(); i++) {
                if (chargeDetailId.equals(chargeDetails.get(i).getId())) {
                    chargeDetails.remove(i);
                }
            }

            chargeDetailDao.deleteById(chargeDetailId);
            //更新redis对应配置
            redisTemplate.opsForHash().put(KEY + charge.getOrganizationId(), charge.getTerminalModel(), JSON.toJSONString(chargeDetails));
        }
    }


    public void delete(Long id) {
        chargeDetailDao.createLambdaQuery().andEq(ChargeDetail::getChargeId, id).delete();
        Charge charge = chargeDao.single(id);
        if (Objects.isNull(charge)) {
            return;
        }
        chargeDao.deleteById(id);
        redisTemplate.opsForHash().delete(KEY + charge.getOrganizationId(), charge.getTerminalModel());
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public PageQuery<PrePaidVM> pageQueryPrePaid(PrepaidQuery query) {
        PageQuery<PrePaidVM> pageQuery = query.toPageQuery();
        chargeDao.pageQueryPrePaid(pageQuery);
        List<PrePaidVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        list.forEach(prePaidVM -> {
            Object json = redisTemplate.opsForHash().get(KEY + prePaidVM.getRootOrgId(), prePaidVM.getTerminalModel());
            if (Objects.isNull(json)) {
                return;
            }
            List<ChargeDetail> chargeDetails = JSONArray.parseArray(json.toString(), ChargeDetail.class);
            if (chargeDetails.isEmpty()) {
                return;
            }
            //若是续费，重新设置服务期开始和结束时间 方便统一计算
            String serviceStartDate = prePaidVM.getServiceStartDate();
            String serviceEndDate = prePaidVM.getServiceEndDate();
            if (prePaidVM.getFlag() == 2) {
                String day = LocalDateTimeUtils.formatDay(System.currentTimeMillis(), "yyyyMMdd");
                String end = LocalDateTimeUtils.addMonth(prePaidVM.getServicePeriod(), "yyyyMMdd", day);
                prePaidVM.setServiceStartDate(day);
                prePaidVM.setServiceEndDate(end);
                prePaidVM.setRenew(true);
            }else{
                prePaidVM.setFee(true);
            }
            Integer serviceStartMon = Integer.parseInt(prePaidVM.getServiceStartDate().substring(0, 6));
            Integer serviceEndMon = Integer.parseInt(prePaidVM.getServiceEndDate().substring(0, 6));
            BigDecimal totalFee = (BigDecimal) calculateFee(chargeDetails, serviceStartMon, serviceEndMon).get(FEE);
            prePaidVM.setServiceFee(totalFee);
            prePaidVM.setServiceStartDate(serviceStartDate);
            prePaidVM.setServiceEndDate(serviceEndDate);
        });
        return pageQuery;
    }

    private Map<String, Object> calculateFee(List<ChargeDetail> chargeDetails, Integer serviceStartMon, Integer serviceEndMon) {
        HashMap<String, Object> result = Maps.newHashMap();
        BigDecimal totalFee = BigDecimal.valueOf(0);
        int totalMon = 0;
        for (int i = 0; i < chargeDetails.size(); i++) {
            BigDecimal singleFee = BigDecimal.valueOf(0);
            int validMon = 0;
            ChargeDetail chargeDetail = chargeDetails.get(i);
            BigDecimal fee = chargeDetail.getFee();
            //若服务费配置结束日期为null 说明至今有效 若 配置开始时间 < 服务期开始时间，说明整个服务期内此费用配置有效
            //若配置开始时间 > 服务开始时间 费用从配置开始时间开始算起
            if (Objects.isNull(chargeDetail.getEnd())) {
                if (serviceStartMon <= chargeDetail.getBegin() && serviceEndMon >= chargeDetail.getBegin()) {
                    int duration = LocalDateTimeUtils.monthDuration("yyyyMMdd", chargeDetail.getBegin() + "01", serviceEndMon + "01") + 1;
                    singleFee = fee.multiply(BigDecimal.valueOf(duration));
                    validMon = duration;
                } else {
                    int duration = serviceEndMon - serviceStartMon;
                    singleFee = fee.multiply(BigDecimal.valueOf(duration));
                    validMon = duration;
                }
            } else {
                //若费用配置日期期间 范围包含车辆服务期范围
                if (chargeDetail.getBegin() <= serviceStartMon && chargeDetail.getEnd() >= serviceEndMon) {
                    int monthDuration = LocalDateTimeUtils.monthDuration("yyyyMMdd", serviceStartMon + "01", serviceEndMon + "01");
                    singleFee = fee.multiply(BigDecimal.valueOf(monthDuration));
                    validMon = monthDuration;
                } else if (serviceEndMon >= chargeDetail.getBegin() && serviceEndMon <= chargeDetail.getEnd() && serviceStartMon < chargeDetail.getBegin()) {
                    //若车辆服务期后半部分被包含在费用配置日期内
                    int monthDuration = LocalDateTimeUtils.monthDuration("yyyyMMdd", chargeDetail.getBegin() + "01", serviceEndMon + "01");
                    singleFee = fee.multiply(BigDecimal.valueOf(monthDuration));
                    validMon = monthDuration;
                } else if (serviceEndMon > chargeDetail.getEnd() && serviceStartMon <= chargeDetail.getEnd() && serviceStartMon >= chargeDetail.getBegin()) {
                    //若车辆服务期前半部分被包含在费用配置日期内
                    int monthDuration = LocalDateTimeUtils.monthDuration("yyyyMMdd", serviceStartMon + "01", chargeDetail.getEnd() + "01");
                    singleFee = fee.multiply(BigDecimal.valueOf(monthDuration));
                    validMon = monthDuration;
                } else if (serviceStartMon <= chargeDetail.getBegin() && serviceEndMon >= chargeDetail.getEnd()) {
                    //若车辆服务期范围 包含 费用配置日期范围
                    int monthDuration = LocalDateTimeUtils.monthDuration("yyyyMMdd", chargeDetail.getBegin() + "01", chargeDetail.getEnd() + "01");
                    singleFee = fee.multiply(BigDecimal.valueOf(monthDuration));
                    validMon = monthDuration;
                }
            }
            totalFee = totalFee.add(singleFee);
            totalMon += validMon;

        }
        result.put(FEE, totalFee);
        result.put(MON_COUNT, totalMon);
        return result;
    }

    public PageQuery<AfterPaidVM> afterPaidQuery(AfterPaidQuery query) {
        Integer beginMon = Integer.parseInt(DateFormatUtil.formatDate("yyyyMM", query.getBeginTime()));
        Integer endMon = Integer.parseInt(DateFormatUtil.formatDate("yyyyMM", query.getEndTime()));
        PageQuery<AfterPaidVM> pageQuery = query.toPageQuery();
        chargeDao.pageQueryAfterPaid(pageQuery);
        List<AfterPaidVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        for (int i = 0; i < list.size(); i++) {
            AfterPaidVM afterPaidVM = list.get(i);
            afterPaidVM.setChargeMon(LocalDateTimeUtils.monthDuration("yyyyMMdd", beginMon + "01", endMon + "01") + 1);
            Integer serviceStartMon = Integer.parseInt(afterPaidVM.getServiceStartDate().substring(0, 6));
            Integer serviceEndMon = Integer.parseInt(afterPaidVM.getServiceEndDate().substring(0, 6));
            //获取车辆对应收费配置
            Object json = redisTemplate.opsForHash().get(KEY + afterPaidVM.getRootOrgId(), afterPaidVM.getTerminalModel());
            if (Objects.isNull(json)) {
                continue;
            }
            List<ChargeDetail> chargeDetails = JSONArray.parseArray(json.toString(), ChargeDetail.class);
            if (ListUtil.isEmpty(chargeDetails)) {
                continue;
            }
            afterPaidVM.setFee(chargeDetails.get(0).getFee());
            Map<String, Object> calculateRes = Maps.newHashMap();
            if (serviceStartMon < beginMon && serviceEndMon >= beginMon && serviceEndMon <= endMon) {
                //搜索时间区间只包含车辆服务期后半部分 将服务期以搜索开始时间分为俩部分
                afterPaidVM.setNotChargeMon(0);
                afterPaidVM.setAlreadyChargedMon(LocalDateTimeUtils.monthDuration("yyyyMMdd", serviceStartMon + "01", beginMon + "01") );
                calculateRes = calculateFee(chargeDetails, beginMon, serviceEndMon + 1);
            } else if (serviceStartMon >= beginMon && serviceStartMon <= endMon && serviceEndMon >= endMon) {
                //搜索时间区间只包含车辆服务期前半部分 将服务期以搜索结束时间分为俩部分
                afterPaidVM.setAlreadyChargedMon(0);
                afterPaidVM.setNotChargeMon(LocalDateTimeUtils.monthDuration("yyyyMMdd", endMon + "01", serviceEndMon + "01") - 1);
                calculateRes = calculateFee(chargeDetails, serviceStartMon, endMon + 1);
            } else if (serviceStartMon < beginMon && serviceEndMon > endMon) {
                //若服务期包含搜索时间区间
                afterPaidVM.setAlreadyChargedMon(LocalDateTimeUtils.monthDuration("yyyyMMdd", serviceStartMon + "01", beginMon + "01"));
                afterPaidVM.setNotChargeMon(LocalDateTimeUtils.monthDuration("yyyyMMdd", endMon + "01", serviceEndMon + "01") - 1);
                calculateRes = calculateFee(chargeDetails, beginMon, endMon + 1);
            } else if (serviceStartMon >= beginMon && serviceEndMon <= endMon) {
                //若服务期被包含于搜索时间区间
                afterPaidVM.setAlreadyChargedMon(0);
                afterPaidVM.setNotChargeMon(0);
                calculateRes = calculateFee(chargeDetails, beginMon, endMon + 1);
            }
            afterPaidVM.setChargeFee((BigDecimal) calculateRes.get(FEE));
            //afterPaidVM.setChargeMon((int) calculateRes.get(MON_COUNT));
        }
        return pageQuery;
    }

    public PageQuery<ServiceExpireWarnVM> pageQueryServiceWarn(PrepaidQuery query) {
        PageQuery<ServiceExpireWarnVM> pageQuery = query.toPageQuery();
        chargeDao.pageQueryServiceWarn(pageQuery);
        return pageQuery;
    }

    /**
     * 获取服务到期车辆数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public Integer dashboardWarn(Integer beginDate, Integer endDate) {
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            return null;
        }
        Long userID = BaseContextHandler.getUserID();
        String vehicleExpireNum = redisTemplate.opsForValue().get(VEHICLE_EXPIRE_NUM + userID);
        String closeCount = redisTemplate.opsForValue().get(VEHICLE_EXPIRE_CLOSE_COUNT + userID);
        if (Objects.isNull(vehicleExpireNum)) {
            //计算当前时间到第二天开始时间的 秒，设置redis key 过期时间
            long second = LocalDateTimeUtils.secondToTomorrow();
            Integer warnCount = chargeDao.warnCount(BaseContextHandler.getOrgId(), beginDate, endDate);
            redisTemplate.opsForValue().set(VEHICLE_EXPIRE_NUM + userID, String.valueOf(warnCount), second, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(VEHICLE_EXPIRE_CLOSE_COUNT + userID, String.valueOf(0), second, TimeUnit.SECONDS);
            return warnCount;
        }
        if (Objects.nonNull(closeCount) && Integer.parseInt(closeCount) > 0) {
            return null;
        }
        return Integer.parseInt(vehicleExpireNum);
    }

    /**
     * 服务到期提醒  关闭弹框
     */
    public void closeWarn() {
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            return;
        }
        Long userID = BaseContextHandler.getUserID();
        String closeCount = redisTemplate.opsForValue().get(VEHICLE_EXPIRE_CLOSE_COUNT + userID);
        if (Objects.isNull(closeCount)) {
            return;
        }
        //记录关闭次数 +1
        redisTemplate.opsForValue().increment(VEHICLE_EXPIRE_CLOSE_COUNT + userID);
    }

}
