package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

@Data
public class EngineDataVM {
    private List<EngineRuntime> runtimeList;
    private List<EngineSpeed> speedList;
}
