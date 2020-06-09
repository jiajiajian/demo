package cn.com.tiza.config;

import cn.com.tiza.annotation.BusinessLogAnnotation;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.BusinessLogDao;
import cn.com.tiza.domain.BusinessLog;
import cn.com.tiza.web.rest.vm.VehicleTerminalVM;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务日志
 *
 * @author tz0920
 */
@Slf4j
@Component
@Aspect
public class BusinessLogConfiguration {
    @Autowired
    private BusinessLogDao businessLogDao;


    @AfterReturning(value = "@annotation(businessLogAnnotation)", returning = "list")
    public void insertBusinessLog(BusinessLogAnnotation businessLogAnnotation, List<VehicleTerminalVM> list) {
        if (list.isEmpty()) {
            return;
        }
        List<BusinessLog> collect = list.stream().map(vm -> BusinessLog.builder()
                .vin(vm.getVin())
                .oldVin(vm.getOldVin())
                .oldTerminal(vm.getOldTerminal())
                .oldSimcard(vm.getOldSimCard())
                .simcard(vm.getSimCard())
                .terminal(vm.getTerminalCode())
                .organizationId(vm.getOrganizationId())
                .createTime(System.currentTimeMillis())
                .createUserRealname(BaseContextHandler.getName())
                .createUserAccount(BaseContextHandler.getLoginName())
                .operateType(businessLogAnnotation.value())
                .renewalMon(vm.getRenewalMon())
                .build()
        ).collect(Collectors.toList());
        businessLogDao.insertBatch(collect);
    }
}
