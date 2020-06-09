package cn.com.tiza.web.rest;

import cn.com.tiza.dto.CommandDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author villas
 */
@FeignClient(value = "feisi-terminal", fallback = CmdLogClientFallBack.class)
public interface CmdLogClient {

    /**
     * 新增指令记录
     *
     * @param dto dto
     * @return 指令表插入记录的id
     */
    @PostMapping("/cmd/command/add")
    Long recordCommand(@RequestBody CommandDto dto);
}
