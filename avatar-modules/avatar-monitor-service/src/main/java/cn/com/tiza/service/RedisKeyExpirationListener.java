package cn.com.tiza.service;

import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.web.rest.VehicleClient;
import cn.com.tiza.web.rest.dto.WorkConditionVM;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tz0920
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final String KEY = "FAULT_";
    private final String REAL = "REAL_";
    private final String BEGIN_TIME = "BEGIN_TIME";
    private final String SPN_FMI = "SPN_FMI";
    private final String TLA = "TLA";
    private final String VIN = "VIN";
    private final String NUM = "NUM";

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private VehicleClient vehicleClient;

    @Autowired
    private AlarmHistoryDao alarmHistoryDao;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        //message.toString()可以获取失效的key
        String key = message.toString();
        //真正key 即 hash 的 key
        String realKey = REAL + key;
        if (StringUtils.hasText(key) && key.startsWith(KEY)) {
            log.info("expiredKey----------{}", realKey);
            Object o = redisTemplate.opsForHash().get(realKey, BEGIN_TIME);
            Object o1 = redisTemplate.opsForHash().get(realKey, VIN);
            Object o2 = redisTemplate.opsForHash().get(realKey, NUM);
            Object o3 = redisTemplate.opsForHash().get(realKey, SPN_FMI);
            Object o4 = redisTemplate.opsForHash().get(realKey, TLA);
            if (Objects.isNull(o) || Objects.isNull(o1) || Objects.isNull(o2)
                    || Objects.isNull(o3)) {
                redisTemplate.delete(realKey);
                return;
            }
            long beginTime = Long.parseLong(String.valueOf(o));
            String vin = String.valueOf(o1);
            String spnFmi = String.valueOf(o3);
            String tla = String.valueOf(o4);
            int num = Integer.parseInt(String.valueOf(o2));
            if (num >= 3) {
                redisTemplate.delete(realKey);
                return;
            }
            Map<String, String> ciData = vehicleClient.ciData(vin);
            if (Objects.isNull(ciData)) {
                redisTemplate.opsForHash().increment(realKey, NUM, 1);
                redisTemplate.opsForValue().set(key, "", 20, TimeUnit.SECONDS);
                return;
            }
            String time = ciData.get("TIME");
            if (Objects.isNull(time)) {
                redisTemplate.opsForHash().increment(realKey, NUM, 1);
                redisTemplate.opsForValue().set(key, "", 20, TimeUnit.SECONDS);
                return;
            }
            if (beginTime >= Long.parseLong(time)) {
                redisTemplate.opsForHash().increment(realKey, NUM, 1);
                redisTemplate.opsForValue().set(key, "", 20, TimeUnit.SECONDS);
                return;
            }
            //将ciData解析持久化至DB
            WorkConditionVM trackData = vehicleClient.getTrackData(vin);
            AlarmHistory single = alarmHistoryDao.createLambdaQuery()
                    .andEq(AlarmHistory::getVin, vin)
                    .andEq(AlarmHistory::getBeginTime, beginTime)
                    .andEq(AlarmHistory::getAlarmType, AlarmType.FAULT)
                    .andEq(AlarmHistory::getSpnFmi, spnFmi)
                    .single();
            single.setRecentlyCondition(JsonMapper.defaultMapper().toJson(trackData.getItemVMList()));
            alarmHistoryDao.updateById(single);
            //将redis key 删除
            redisTemplate.delete(realKey);
            redisTemplate.delete(key);
        }
    }
}
