package cn.com.tiza.service;

import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dao.DictionaryDao;
import cn.com.tiza.domain.BaseDictionary;
import cn.com.tiza.web.rest.vm.GeocodeVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author tz0920
 */
@Slf4j
@Service
public class VehicleLocationService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private DictionaryDao dictionaryDao;


    /**
     * 通过经纬度在高德地图获取详细地址
     *
     * @param lon lat 经纬,纬度（经度在前，纬度在后）
     * @return 详细地址
     */
    public String getLocationByLonAndLat(String lon, String lat) {
        String address = "";
        if (Objects.isNull(lon) || Objects.isNull(lat)) {
            return address;
        }
        //获取地图key
        BaseDictionary dictionary = dictionaryDao.findDictionaryByItemName("MAP_KEY");
        if (Objects.isNull(dictionary) || Objects.isNull(dictionary.getRemark())) {
            return address;
        }
        String url = String.format(properties.getAMap().getRegeocoder(), lon + "," + lat, dictionary.getRemark());
        GeocodeVM geocodeVM = this.restTemplate.getForObject(url, GeocodeVM.class);
        if (1 != geocodeVM.getStatus()) {
            return address;
        } else {
            return String.valueOf(geocodeVM.getRegeocode().get("formatted_address"));
        }
    }
}
