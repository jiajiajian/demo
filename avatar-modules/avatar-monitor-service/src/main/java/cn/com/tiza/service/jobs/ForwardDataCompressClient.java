package cn.com.tiza.service.jobs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 */
@FeignClient("feisi-protocol")
public interface ForwardDataCompressClient {
    /**
     * 转发任务
     * @return
     */
    @GetMapping("/exportTask/forwardDataCompressJob")
    void forwardDataCompressJob();
}
