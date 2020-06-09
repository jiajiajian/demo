package cn.com.tiza.web.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author tz0920
 */
@FeignClient(value = "feisi-TERMINAL", fallback = HystrixSimcardClientFallBack.class)
public interface SimcardClient {

    /**
     * 根据终端 修改SIM卡状态 且删除该终端
     *
     * @param terminalId
     * @param cardStatus SIM卡需要被修改的状态
     * @return
     */
    @PutMapping("/sim/card/resetCardStatusAndDeleteTerminal/{terminalId}/{cardStatus}")
    boolean resetCardStatusAndDeleteTerminal(@PathVariable("terminalId") Long terminalId,
                                             @PathVariable("cardStatus") Integer cardStatus);
}
