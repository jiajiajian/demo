package cn.com.tiza.web.rest;

import cn.com.tiza.dto.CommandDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author villas
 */
@Slf4j
@Component
public class CmdLogClientFallBack implements CmdLogClient {

    @Override
    public Long recordCommand(CommandDto dto) {
        log.error("CmdLogClientFallBack recordCommand is error, param is {}", dto);
        throw new NullPointerException("cmd record is is null");
    }
}
