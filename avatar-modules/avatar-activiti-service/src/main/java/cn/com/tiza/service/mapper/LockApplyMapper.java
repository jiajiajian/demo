package cn.com.tiza.service.mapper;


import cn.com.tiza.constant.ApplyStateEnum;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.LockApply;
import cn.com.tiza.service.dto.LockApplyDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.vip.vjtools.vjkit.time.DateFormatUtil.formatDate;

/**
*  Mapper
* gen by beetlsql 2020-04-27
* @author tiza
*/
@Component
public class LockApplyMapper {

    public LockApply dtoToEntity(LockApplyDto dto) {
        if(dto == null) {
            return null;
        }
        LockApply entity = new LockApply();
        entity.setState(ApplyStateEnum.no_approved.getState());
        entity.setApplyCode(generateApplyNo());
        entity.setApplyUser(BaseContextHandler.getLoginName());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setInstanceId(dto.getInstanceId());
        entity.setOrgId(BaseContextHandler.getOrgId());
        entity.setReason(dto.getReason());
        entity.setExpireTime(nextMonth());
        return entity;
    }

    private  static long nextMonth() {
        LocalDateTime now = LocalDateTime.now().plusMonths(1);
        return now.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    private static String generateApplyNo(){
        StringBuilder sb = new StringBuilder("SQ");
        String dateStr = formatDate("yyyyMMddHHmmss", System.currentTimeMillis());
        sb.append(dateStr);
        return sb.toString();
    }

}
