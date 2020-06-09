package cn.com.tiza.web.rest;

import org.springframework.stereotype.Component;

/**
 * @author tz0920
 */
@Component
public class HystrixSimcardClientFallBack implements SimcardClient {
    @Override
    public boolean resetCardStatusAndDeleteTerminal(Long terminalId,Integer cardStatus) {
        return false;
    }
}
