package cn.com.tiza.web.rest;

import cn.com.tiza.service.dto.FileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 文件接口，只保存excel等在db中的文件
 *
 * @author tiza
 */
@FeignClient("feisi-files")
public interface FileApiClient {

    @PostMapping("/files")
    String save(@RequestBody FileDto fileDto);

}
