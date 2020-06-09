package cn.com.tiza.web.rest;

import cn.com.tiza.dto.QueryCommand;
import cn.com.tiza.dto.RestCreateDataQueryResult;
import cn.com.tiza.dto.RestDataQueryResult;
import cn.com.tiza.grampus.GrampusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 历史数据查询
 *
 * @author tiza
 */
@RestController
@RequestMapping("data")
public class HBaseController {

    @Autowired
    private GrampusClient grampusClient;

    @PostMapping("/{apiKey}")
    public RestCreateDataQueryResult createDataQuery(@PathVariable("apiKey") String apiKey,
                                                     @RequestBody QueryCommand command) {
        return grampusClient.createDataQuery(apiKey, command);
    }

    @PostMapping("/{apiKey}/row")
    public RestCreateDataQueryResult createDataQueryRow(@PathVariable("apiKey") String apiKey,
                                                        @RequestBody QueryCommand command) {
        return grampusClient.createDataQueryRaw(apiKey, command);
    }

    @PostMapping("/{apiKey}/tracker")
    public RestCreateDataQueryResult createDataQueryTracker(@PathVariable("apiKey") String apiKey,
                                                            @RequestBody QueryCommand command) {
        return grampusClient.createDataQueryTrack(apiKey, command);
    }

    @GetMapping("/{queryId}/{apiKey}")
    public RestDataQueryResult dataQuery(@PathVariable("apiKey") String apiKey,
                                         @PathVariable("queryId") Integer queryId) {
        return grampusClient.dataQuery(apiKey, queryId, 1000);
    }

    @GetMapping("/{queryId}/{apiKey}/{row}")
    public RestDataQueryResult dataQuery(@PathVariable("apiKey") String apiKey,
                                         @PathVariable("queryId") Integer queryId,
                                         @PathVariable("row") Integer row) {
        return grampusClient.dataQuery(apiKey, queryId, row);
    }


    @DeleteMapping("/{queryId}/{apiKey}")
    public void closeDataQuery(@PathVariable("apiKey") String apiKey,
                               @PathVariable("queryId") Integer queryId) {
        grampusClient.closeDataQuery(apiKey, queryId);
    }
}
