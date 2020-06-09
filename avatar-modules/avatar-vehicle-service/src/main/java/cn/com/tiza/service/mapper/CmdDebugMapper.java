package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmItemDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.service.dto.CmdDebugDto;
import cn.com.tiza.web.rest.vm.CmdDebugVM;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Component
public class CmdDebugMapper {
    @Autowired
    private AlarmItemDao alarmItemDao;

    public CmdDebug dtoToEntity(CmdDebugDto dto) {
        if (dto == null) {
            return null;
        }
        CmdDebug entity = new CmdDebug();
        entity.setCmd(StringUtils.join(dto.getCmdList(),","));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setVehicleTypeId(dto.getVehicleTypeId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public CmdDebugVM toVM(CmdDebug entity) {
        if (entity == null) {
            return null;
        }
        CmdDebugVM vm = new CmdDebugVM();
        vm.setId(entity.getId());
        vm.setCmd(entity.getCmd());
        if (StringUtils.isNotEmpty(vm.getCmd())) {
            //将code转换为name 数据从数据字典获取
            Map<String, String> itemMap = alarmItemDao.findDictItem("CMD_DEBUG_ITEM").stream()
                    .collect(Collectors.toMap(e -> e.get("itemCode"), e -> e.get("itemName"), (k1, k2) -> k2));
            Map<String, String> lockMap = alarmItemDao.findDictItem("LOCK").stream()
                    .collect(Collectors.toMap(e -> e.get("itemCode"), e -> e.get("itemName"), (k1, k2) -> k2));
            Map<String, String> resultMap = new HashMap<>(16);
            resultMap.putAll(itemMap);
            resultMap.putAll(lockMap);
            List<HashMap<String, String>> cmdList = Arrays.asList(vm.getCmd().split(","))
                    .stream().map(cmdStr -> {
                        HashMap<String, String> item = new HashMap<>();
                        item.put("itemCode", cmdStr);
                        item.put("itemName", resultMap.get(cmdStr));
                        return item;
                    }).collect(Collectors.toList());
            vm.setCmdList(cmdList);
            vm.setCmdNum(cmdList.size());
        }
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setVehicleTypeId(entity.getVehicleTypeId());
        return vm;
    }

    public List<CmdDebugVM> entitiesToVMList(List<CmdDebug> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
