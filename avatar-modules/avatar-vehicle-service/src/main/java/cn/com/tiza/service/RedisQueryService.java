package cn.com.tiza.service;

import cn.com.tiza.service.dto.WorkConditionDto;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * redis Service
 */
@Service
public class RedisQueryService {

    private static final String VEHICLE_PI_KEY = "GPRS10_PI_%s";
    private static final String VEHICLE_STA_KEY = "GPRS10_STA_%s";
    private static final String VEHICLE_CI_KEY = "GPRS10_CI_%s";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /***
     * 位置
     *
     * @param vin
     * @return
     */
    public Map<String, Object> piData(String vin) {
        if (StringUtils.isEmpty(vin)) {
            return null;
        }
        String value = stringRedisTemplate.opsForValue().get(String.format(VEHICLE_PI_KEY, vin));
        return JsonMapper.defaultMapper().fromJson(value, Map.class);
    }

    /***
     * 状态
     *
     * @param vin
     * @return
     */
    public Map<String, Object> staData(String vin) {
        if (StringUtils.isEmpty(vin)) {
            return null;
        }
        String value = stringRedisTemplate.opsForValue().get(String.format(VEHICLE_STA_KEY, vin));
        return JsonMapper.defaultMapper().fromJson(value, Map.class);
    }

    /***
     * 工况
     *
     * @param vin
     * @return
     */
    public Map<String, String> ciData(String vin) {
        if (StringUtils.isEmpty(vin)) {
            return null;
        }
        String value = stringRedisTemplate.opsForValue().get(String.format(VEHICLE_CI_KEY, vin));
        List<WorkConditionDto> conditionList = JSONArray.parseArray(value, WorkConditionDto.class);
        if (Objects.isNull(conditionList)){
            return null;
        }
        return conditionList.stream().filter(workConditionDto->!Objects.isNull(workConditionDto.getNAME())).collect(Collectors.toMap(e -> e.getNAME(), e -> e.getVALUE(), (k1, k2) -> k2));
    }

    public Map<String, Object> realtime(String vin) {
        Map<String, Object> map = Maps.newHashMap();
        map.putAll(piData(vin));
        map.putAll(staData(vin));
        map.putAll(ciData(vin));
        return map;
    }


    public void delete(String vin) {
        stringRedisTemplate.delete(String.format(VEHICLE_PI_KEY, vin));
        stringRedisTemplate.delete(String.format(VEHICLE_STA_KEY, vin));
        stringRedisTemplate.delete(String.format(VEHICLE_CI_KEY, vin));
    }

}