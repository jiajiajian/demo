package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

@Data
public class WorkTimeData {
    private List<WorkTimeDetailVM>  workTimeDetailVMList;
    private List<MachineWorkDetailVM> machineWorkDetailVMList;
}
