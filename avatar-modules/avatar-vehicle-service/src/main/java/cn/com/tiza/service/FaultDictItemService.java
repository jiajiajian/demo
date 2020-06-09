package cn.com.tiza.service;


import cn.com.tiza.dao.FaultDictItemDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.FaultDictItem;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.dto.FaultDictItemDto;
import cn.com.tiza.service.dto.FaultDictItemImportDto;
import cn.com.tiza.service.dto.FaultDictItemQuery;
import cn.com.tiza.service.mapper.FaultDictItemMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.FaultDictItemVM;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FaultDictItemService {

    @Autowired
    private FaultDictItemDao faultDictItemDao;

    @Autowired
    private FaultDictItemMapper faultDictItemMapper;

    @Autowired
    private VehicleDao vehicleDao;

    public PageQuery<FaultDictItemVM> findAll(FaultDictItemQuery query) {
        PageQuery<FaultDictItemVM> pageQuery = query.toPageQuery();
        faultDictItemDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<FaultDictItem> get(Long id) {
        return Optional.ofNullable(faultDictItemDao.single(id));
    }


    public FaultDictItem create(FaultDictItemDto command) {
        //是否需要校验tla
        FaultDictItem faultDictItem = checkProperty(command);

        if (Objects.nonNull(faultDictItem)) {
            throw new BadRequestException(ErrorConstants.FAULT_DICT_ITEM_CAN_NOT_BE_CREAT);
        }
        FaultDictItem entity = faultDictItemMapper.dtoToEntity(command);
        faultDictItemDao.insert(entity);
        return entity;
    }

    public Optional<FaultDictItem> update(Long id, FaultDictItemDto command) {
        return get(id).map(entity -> {
            FaultDictItem faultDictItem = checkProperty(command);

            if (Objects.nonNull(faultDictItem) && !faultDictItem.getId().equals(id)) {
                throw new BadRequestException(ErrorConstants.FAULT_DICT_ITEM_CAN_NOT_BE_CREAT);
            }
            entity.setFmi(command.getFmi());
            entity.setFmiName(command.getFmiName());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setSpn(command.getSpn());
            entity.setSpnName(command.getSpnName());
            entity.setTlaId(command.getTlaId());
            faultDictItemDao.updateById(entity);
            return entity;
        });
    }

    private FaultDictItem checkProperty(FaultDictItemDto command) {
        //是否需要校验tla
        Long count = vehicleDao.tlaCount(command.getOrganizationId());
        FaultDictItem faultDictItem;
        if (count > 0) {
            faultDictItem = faultDictItemDao.findBySpnFmiTla(command.getSpn(),
                    command.getFmi(), command.getTlaId(), command.getOrganizationId());
        } else {
            faultDictItem = faultDictItemDao.findBySpnFmi(command.getSpn(),
                    command.getFmi(), command.getOrganizationId());
        }
        return faultDictItem;
    }

    public void delete(Long id) {
        faultDictItemDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public void checkImport(ExcelReader reader, List<FaultDictItemImportDto> dtoList, Long rootOrgId) {
        List<String> spnFmiTlaList = faultDictItemDao.spnFmiTlaQueryByRootOrg(rootOrgId)
                .stream()
                .map(vm -> vm.getSpn() + "." + vm.getFmi() + "." + vm.getTla())
                .collect(Collectors.toList());
        List<String> tlaList = faultDictItemDao.tlaList(rootOrgId);
        HashSet<String> set = Sets.newHashSet();
        StringBuilder builder = new StringBuilder();
        dtoList.forEach(dto -> {
            StringBuilder append;
            if (tlaList.size() > 0) {
                append = builder.append(dto.getSpn()).append(dto.getFmi()).append(dto.getTla());
            } else {
                append = builder.append(dto.getSpn()).append(dto.getFmi());
            }
            String str = append.toString();
            if (set.contains(str)) {
                reader.addCellError(dto, FaultDictItemImportDto.IDX_SPN, "数据不合法");
            } else {
                set.add(str);
            }
            if (spnFmiTlaList.contains(str)) {
                reader.addCellError(dto, FaultDictItemImportDto.IDX_SPN, "数据不合法");
            }
            //校验tla
            if (Objects.nonNull(dto.getTla()) && !tlaList.contains(dto.getTla())) {
                reader.addCellError(dto, FaultDictItemImportDto.IDX_TLA, "TLA不存在");
            }
            builder.delete(0, str.length());
        });
    }

    public void importItem(List<FaultDictItemImportDto> dtoList, Long rootOrgId) {
        if (dtoList.isEmpty()) {
            return;
        }

        List<FaultDictItem> collect = dtoList.stream()
                .map(dto -> {
                    FaultDictItem item = new FaultDictItem();
                    item.setOrganizationId(rootOrgId);
                    item.setSpn(dto.getSpn());
                    item.setFmi(dto.getFmi());
                    item.setSpnName(dto.getSpnName());
                    item.setFmiName(dto.getFmiName());
                    Long tlaId = faultDictItemDao.tlaId(rootOrgId, dto.getTla());
                    item.setTlaId(tlaId);
                    return item;
                }).collect(Collectors.toList());
        faultDictItemDao.insertBatch(collect);
    }
}
