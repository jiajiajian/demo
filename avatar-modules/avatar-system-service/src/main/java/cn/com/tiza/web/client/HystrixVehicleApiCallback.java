package cn.com.tiza.web.client;

import org.springframework.stereotype.Component;

@Component
public class HystrixVehicleApiCallback implements VehicleApiClient {

    @Override
    public Long countVehicleByOrg(Long orgId) {
        return 0L;
    }
}
