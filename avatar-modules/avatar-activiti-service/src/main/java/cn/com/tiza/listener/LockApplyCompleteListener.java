package cn.com.tiza.listener;

import cn.com.tiza.constant.ApplyStateEnum;
import cn.com.tiza.dao.LockApplyDao;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LockApplyCompleteListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Autowired
    private LockApplyDao lockApplyDao;

    @Override
    public void notify(DelegateTask delegateTask) {
        if(Objects.nonNull(delegateTask.getVariable("action"))){
            int action =  (int)delegateTask.getVariable("action");
            if ( Objects.nonNull(action) && action == 1) {
                String applyCode = delegateTask.getExecution().getProcessInstanceBusinessKey();
                lockApplyDao.updateStateByCode(applyCode, ApplyStateEnum.success.getState());
            }
        }
    }
}
