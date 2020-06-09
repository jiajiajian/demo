package cn.com.tiza.web.rest;

import cn.com.tiza.GrampusApplication;
import cn.com.tiza.dto.RestResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = GrampusApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TerminalControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
    void register() {
        String apiKey = "CA3D67C35B5D27192583C214A9A05BB3";
        String vin = "L2018020816888881";
        String terminalType = "gb17691test";
//        RestResult result = restTemplate.postForObject("/terminals/{0}/{1}/{2}", null, RestResult.class, terminalType, vin, apiKey);
        restTemplate.delete("/terminals/{0}/{1}/{2}", terminalType, vin, apiKey);
    }
}