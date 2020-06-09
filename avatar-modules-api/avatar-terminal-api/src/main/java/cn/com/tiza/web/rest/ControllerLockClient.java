package cn.com.tiza.web.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "feisi-TERMINAL")
public interface ControllerLockClient {
    /**
     * 获取控制器锁车最终结果
     */
    @PutMapping("lock/schedule")
    void scheduleControllerLock();
}
