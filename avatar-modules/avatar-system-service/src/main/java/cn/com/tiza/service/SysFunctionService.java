package cn.com.tiza.service;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AuthorizeDao;
import cn.com.tiza.dao.SysFunctionDao;
import cn.com.tiza.domain.SysFunction;
import cn.com.tiza.service.mapper.SysFunctionMapper;
import cn.com.tiza.web.rest.vm.SysFunctionVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiajian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysFunctionService {
    final Long ROOT = -1L;

    @Autowired
    private SysFunctionDao sysFunctionDao;

    @Autowired
    private SysFunctionMapper sysFunctionMapper;

    @Autowired
    private AuthorizeDao authorizeDao;

    public Map tree(Long roleId) {
        List<SysFunctionVM> sysFunctionVMS = getSysFunctionVMSByUserId();

        List<Long> longs = authorizeDao.selectByRoleId(roleId);
        Map resultMap = new HashMap(100);
        resultMap.put("data", sysFunctionVMS);
        resultMap.put("checked", longs);
        return resultMap;
    }

    private List<SysFunctionVM> getSysFunctionVMSByUserId() {
        List<SysFunction> all = new ArrayList<>();
        if (Constants.SUPER_ADMIN.equals(BaseContextHandler.getLoginName())) {
            all.addAll(sysFunctionDao.all());
        } else {
            all.addAll(sysFunctionDao.selectAuthorizesByUser(BaseContextHandler.getUserID()));
        }
        return sysFunctionMapper.sysFunctionVMSToTree(all, ROOT);
    }

    /**
     * 查询权限数据
     *
     * @return
     */
    public Set<Long> getFunctionIdsByUser(Long uid, String loginName) {
        List<SysFunction> functions = null;
        String userType = BaseContextHandler.getUserType();
        if ("ADMIN".equals(userType)) {
            functions = sysFunctionDao.all();
        } else {
            functions = sysFunctionDao.selectAuthorizesByUser(uid);
        }
        return functions.stream().map(SysFunction::getId).collect(Collectors.toSet());
    }
}
