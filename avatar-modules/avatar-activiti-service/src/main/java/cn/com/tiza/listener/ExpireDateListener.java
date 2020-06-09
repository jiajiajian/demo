package cn.com.tiza.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ExpireDateListener implements Serializable, ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {

    }
}
