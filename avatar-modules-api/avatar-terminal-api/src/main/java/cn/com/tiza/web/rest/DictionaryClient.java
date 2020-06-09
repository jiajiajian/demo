package cn.com.tiza.web.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient("feisi-TERMINAL")
public interface DictionaryClient {

    @GetMapping("terminal/options/{code}")
    List<Map<String, Object>> getOptionsByType(@PathVariable("code") String code);
}
