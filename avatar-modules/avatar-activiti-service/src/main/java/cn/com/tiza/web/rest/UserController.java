package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.Query;
import cn.com.tiza.dto.UserType;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户和组
 *
 * @author villas
 */
@RequestMapping("/identity")
@RestController
public class UserController {

    @Autowired
    private IdentityService identityService;

   @Autowired
   private ManagementService managementService;

    @Autowired
    DictionaryClient dictionaryClient;

    @GetMapping("/users")
    public List<User> list(Query query) {
        StringBuilder builder = new StringBuilder();
        builder.append("select * from "+ managementService.getTableName(User.class) + " u where u.FIRST_=${loginName}");
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name()) && Objects.isNull(query.getOrganizationId())) {
            builder.append(" and u.ORG_ID_="+ BaseContextHandler.getOrgId());
        }
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            builder.append(" and u.FINANCE_ID_="+BaseContextHandler.getFinanceId());
        }
        builder.append(" and u.LAST_=0");
        return identityService.createNativeUserQuery()
                .sql(builder.toString())
                .list();
    }

    @GetMapping("/groups")
    public List<Group> groupList(Query query) {
        StringBuilder builder = new StringBuilder();
        builder.append("select * from " + managementService.getTableName(Group.class) + " g where 1=1");
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name()) && Objects.isNull(query.getOrganizationId())) {
            builder.append(" and u.ORG_ID_="+ BaseContextHandler.getOrgId());
        }
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            builder.append(" and u.FINANCE_ID_="+BaseContextHandler.getFinanceId());
        }
        return identityService.createNativeGroupQuery()
                .sql(builder.toString())
                .list();
    }

    @GetMapping("/options/{code}")
    public ResponseEntity<List<Map<String, Object>>> getOptionsByType(@PathVariable String code) {
        return ResponseEntity.ok(dictionaryClient.getOptionsByType(code));
    }
}
