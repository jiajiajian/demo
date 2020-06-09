package cn.com.tiza.web.rest;

import cn.com.tiza.Global;
import cn.com.tiza.GrampusApplication;
import cn.com.tiza.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = GrampusApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HBaseControllerTest {

//    @Test
//    void dataQueryReactive(@Autowired WebTestClient webClient) {
//        String apiKey = "CA3D67C35B5D27192583C214A9A05BB3";
//        String vin = "L2018020816888888";
//        Long startTime = System.currentTimeMillis() - 1000 * 60 * 60;
//        Long endTime = System.currentTimeMillis();
//        String terminalType = "gb17691test";
//        QueryCommand command = QueryCommand.builder()
//                .terminalID(vin)
//                .startTime(startTime)
//                .endTime(endTime)
//                //cmdID默认0 查询所有工况数据，保留 2 实时数据、 3 补发数据
//                .cmdID(0)
//                .tableName(Global.rawTableName(terminalType))
//                .build();
//        webClient.post().uri("/data/" + apiKey)
//                .body(command, QueryCommand.class).exchange()
//                .expectBody(RestCreateDataQueryResult.class);
//    }

    @Autowired
    private TestRestTemplate restTemplate;
//    @Test
    void dataQuery() {
        String apiKey = "CA3D67C35B5D27192583C214A9A05BB3";
        String vin = "L2018020816888888";
        Long startTime = System.currentTimeMillis() - 1000 * 60 * 60;
        Long endTime = System.currentTimeMillis();
        String terminalType = "gb17691test";
        QueryCommand command = QueryCommand.builder()
                .terminalID(vin)
                .startTime(startTime)
                .endTime(endTime)
                //cmdID默认0 查询所有工况数据，保留 2 实时数据、 3 补发数据
                .cmdID(0)
                .tableName(Global.rawTableName(terminalType))
                .build();
        RestCreateDataQueryResult result = restTemplate.postForObject("/data/" + apiKey, command, RestCreateDataQueryResult.class);
        System.out.println(result);

        int queryId = result.getQueryId().intValue();

        RestDataQueryResult records = restTemplate.getForObject("/data/" + queryId + "/" + apiKey, RestDataQueryResult.class);
        records.getRecords().forEach(System.out::println);

        restTemplate.delete("/data/{0}/{1}", queryId, apiKey);
    }
}