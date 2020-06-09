package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.fwp.FwpApiClient;
import cn.com.tiza.vm.OriginRelationVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 转发映射管理
 *
 * @author villas 2019-02-21
 */
@Slf4j
@RestController
@RequestMapping("/relations")
public class OriginRelationController {

    @Autowired
    private FwpApiClient fwpApiClient;

    @GetMapping
    public ResponseEntity<OriginRelationVM[]> list() {
//        return null;
        return ResponseEntity.ok(fwpApiClient.relations(BaseContextHandler.getRootOrgId()));
    }

}
