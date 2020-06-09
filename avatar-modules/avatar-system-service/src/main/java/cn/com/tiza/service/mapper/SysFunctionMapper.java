package cn.com.tiza.service.mapper;

import cn.com.tiza.domain.SysFunction;
import cn.com.tiza.util.TreeUtil;
import cn.com.tiza.web.rest.vm.SysFunctionVM;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  jiajian
 */
@Component
public class SysFunctionMapper {

    public SysFunctionVM sysFunctionToVm(SysFunction sysFunction) {
        if (null == sysFunction) {
            return null;
        }
        SysFunctionVM sysFunctionVM = new SysFunctionVM();
        sysFunctionVM.setTitle(sysFunction.getFunctionName());
        sysFunctionVM.setKey(sysFunction.getId());
        sysFunctionVM.setParentId(sysFunction.getParentFunctionId());
        return sysFunctionVM;
    }

    public List<SysFunctionVM> sysFunctionVMSToTree(List<SysFunction> list, long parentId) {
        List<SysFunctionVM> tree = new ArrayList<>();
        list.stream().forEach(sysFunction -> tree.add(sysFunctionToVm(sysFunction)));
        return TreeUtil.buildTreeByRecursive(tree, parentId);
    }

}
