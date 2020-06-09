package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.VehicleRealtimeDao;
import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.dto.VehicleRealtimeDto;
import cn.com.tiza.service.dto.VehicleRealtimeQuery;
import cn.com.tiza.service.dto.WorkTimeDto;
import cn.com.tiza.service.mapper.VehicleRealtimeMapper;
import cn.com.tiza.util.ToolUtil;
import com.google.common.collect.ImmutableMap;
import com.vip.vjtools.vjkit.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class VehicleRealtimeService {

    @Autowired
    private VehicleRealtimeDao vehicleRealtimeDao;

    @Autowired
    private VehicleRealtimeMapper vehicleRealtimeMapper;

    public PageQuery<VehicleRealtime> findAll(VehicleRealtimeQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        vehicleRealtimeDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<VehicleRealtime> get(Long id) {
        return Optional.ofNullable(vehicleRealtimeDao.single(id));
    }

    public VehicleRealtime create(VehicleRealtimeDto command) {
        VehicleRealtime entity = vehicleRealtimeMapper.dtoToEntity(command);
        vehicleRealtimeDao.insert(entity);
        return entity;
    }

    public Optional<VehicleRealtime> update(Long id, VehicleRealtimeDto command) {
        return get(id).map(entity -> {
            entity.setId(command.getId());
            entity.setLock(command.getLock());
            entity.setTotalWorkTime(command.getTotalWorkTime());
            entity.setAcc(command.getAcc());
            entity.setDataUpdateTime(command.getDataUpdateTime());
            entity.setDebugEndTime(command.getDebugEndTime());
            entity.setDebugStartTime(command.getDebugStartTime());
            entity.setDescription(command.getDescription());
            entity.setGps(command.getGps());
            entity.setGpsAddress(command.getGpsAddress());
            entity.setGpsArea(command.getGpsArea());
            entity.setGpsCity(command.getGpsCity());
            entity.setGpsProvince(command.getGpsProvince());
            entity.setGpsTime(command.getGpsTime());
            entity.setLat(command.getLat());
            entity.setLon(command.getLon());
            entity.setVin(command.getVin());
            return entity;
        });
    }

    public void delete(Long id) {
        vehicleRealtimeDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }


    public List<Map<String, Object>> workTimeStatistic(WorkTimeDto dto) {
        Map<String, Long> map = this.workTimeParse(dto);
        List<Map<String, Object>> resultList = new ArrayList<>(map.size());
        map.forEach((k, v) -> resultList.add(ImmutableMap.of("name", k, "value", v)));
        return resultList;
    }

    private Map<String, Long> workTimeParse(WorkTimeDto dto) {
        if (dto.getOrgId() == null) {
            dto.setOrgId(BaseContextHandler.getOrgId());
        }
        List<VehicleRealtime> realtimeList = vehicleRealtimeDao.workTimeStatistic(dto);
        if (ListUtil.isEmpty(realtimeList)) {
            return Collections.emptyMap();
        }

        Map<String, Long> map = new TreeMap<>(Comparator.comparing(s -> Integer.parseInt(s.split("-")[0])));

        Map<String, Long> result = realtimeList.stream()
                .collect(Collectors.groupingBy(r -> this.groupWorkTime(r.getTotalWorkTime()), () -> map,
                        Collectors.summingLong(r -> Optional.ofNullable(r.getTotalWorkTime()).orElse(0L))));

        for (long i = 500; i <= 15000; i += 500) {
            result.put(this.groupWorkTime(i), result.getOrDefault(this.groupWorkTime(i), 0L));
        }
        return result;
    }

    private String groupWorkTime(Long workTime) {
        if (workTime == null || workTime <= 500) {
            return "0-500h";
        } else if (workTime <= 1000) {
            return "500-1000h";
        } else if (workTime <= 1500) {
            return "1000-1500h";
        } else if (workTime <= 2000) {
            return "1500-2000h";
        } else if (workTime <= 3000) {
            return "2000-3000h";
        } else if (workTime <= 4000) {
            return "3000-4000h";
        } else if (workTime <= 5000) {
            return "4000-5000h";
        } else if (workTime <= 6000) {
            return "5000-6000h";
        } else if (workTime <= 7000) {
            return "6000-7000h";
        } else if (workTime <= 8000) {
            return "7000-8000h";
        } else if (workTime <= 9000) {
            return "8000-9000h";
        } else if (workTime <= 10000) {
            return "9000-10000h";
        } else if (workTime <= 11000) {
            return "10000-11000h";
        } else if (workTime <= 12000) {
            return "11000-12000h";
        } else if (workTime <= 13000) {
            return "12000-13000";
        } else if (workTime <= 14000) {
            return "13000-14000";
        } else if (workTime <= 15000) {
            return "14000-15000";
        } else {
            return ">15000h";
        }
    }

    public void workTimeStatisticExport(HttpServletRequest request, HttpServletResponse response,
                                        String fileName, WorkTimeDto dto) throws IOException {
        Map<String, Long> map = this.workTimeParse(dto);
        String[] header = new String[map.size()];

        Iterator<Map.Entry<String, Long>> iterator = map.entrySet().iterator();
        int i = 0;
        Map<String, Long> data = new HashMap<>(ToolUtil.computeMap(map.size()));
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            header[i++] = entry.getKey();
            data.put(entry.getKey(), entry.getValue());
        }
        List<Map<String, Long>> content = new ArrayList<>();
        content.add(data);

        ExcelWriter.exportExcel(request, response, fileName, header, header, content);

    }
}
