//package cn.com.tiza.web.rest;
//
//import cn.com.tiza.context.UserInfo;
//import cn.com.tiza.web.rest.dto.LogCommand;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import javax.validation.Valid;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class HystrixAccountApiFallback implements AccountApiClient {
//
//    @Override
//    public UserInfo parseToken(String token) {
//        return null;
//    }
//
//    @Override
//    public Boolean currentUserOrgIsParent(Long orgId) {
//        return false;
//    }
//
//    @Override
//    public boolean checkPermission(int[] permissions) {
//        return false;
//    }
//
//    @Override
//    public ResponseEntity create(@Valid LogCommand log) {
//        return ResponseEntity.ok().build();
//    }
//
//    @Override
//    public List<String> findUserRoleIds(Long userId) {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public List<Long> findChildOrgIds(Long orgId) {
//        return Collections.emptyList();
//    }
//}
