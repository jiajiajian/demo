package cn.com.tiza.service.mapper;

import cn.com.tiza.domain.Organization;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.OrganizationCommand;
import cn.com.tiza.util.TreeUtil;
import cn.com.tiza.web.rest.vm.OrganizationTreeVM;
import cn.com.tiza.web.rest.vm.OrganizationVM;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * org实体与与dto转换
 *
 * @author tiza
 */
@Component
public class OrganizationMapper {

    public Organization commandToEntity(OrganizationCommand command) {
        if (command == null) {
            return null;
        }

        Organization organization = new Organization();

        organization.setOrgTypeId(command.getOrgTypeId());
        organization.setOrgCode(command.getOrgCode());
        organization.setOrgName(command.getOrgName());
        organization.setAbbrName(command.getAbbrName());
        organization.setParentOrgId(command.getParentOrgId());
        organization.setContactName(command.getContactName());
        organization.setFaxNo(command.getFaxNo());
        organization.setTelephoneNumber(command.getTelephoneNumber());
        organization.setEmailAddress(command.getEmailAddress());
        organization.setProvinceName(command.getProvinceName());
        organization.setCityName(command.getCityName());
        organization.setCountyName(command.getCountyName());
        organization.setContactAddress(command.getContactAddress());
        organization.setRemark(command.getRemark());

        organization.setCreatorInfo();
        organization.setUpdateInfo();
        return organization;
    }

    public OrganizationVM toVM(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationVM orgVM = new OrganizationVM();
        BeanUtils.copyProperties(organization, orgVM);
        return orgVM;
    }

    private OrganizationTreeVM orgToTreeNode(Organization baseOrganization) {
        if (baseOrganization == null) {
            return null;
        }
        OrganizationTreeVM orgVM = new OrganizationTreeVM();
        orgVM.setKey(baseOrganization.getId());
        orgVM.setTitle(baseOrganization.getOrgName());
        orgVM.setParentId(baseOrganization.getParentOrgId());
        return orgVM;
    }

    public List<OrganizationTreeVM> orgToTree(List<Organization> list, Long parentId) {
        List<OrganizationTreeVM> tree = new ArrayList<>();
        list.forEach(obj -> tree.add(orgToTreeNode(obj)));
        return TreeUtil.buildTreeByRecursive(tree, parentId);
    }

    /**
     * 使用递归创建组织树
     *
     * @param treeNodes
     * @return
     */
    public List<OrganizationVM> buildOrgTreeTable(List<OrganizationVM> treeNodes, Object root) {
        List<OrganizationVM> trees = new ArrayList<>();
        for (OrganizationVM treeNode : treeNodes) {
            if (root.equals(treeNode.getParentOrgId())) {
                trees.add(findOrgChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    public OrganizationVM findOrgChildren(OrganizationVM treeNode, List<OrganizationVM> treeNodes) {
        for (OrganizationVM it : treeNodes) {
            if (treeNode.getId().equals(it.getParentOrgId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findOrgChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    public SelectOption toOption(Organization entity) {
        if (entity == null) {
            return null;
        }
        SelectOption option = new SelectOption();
        option.setId(entity.getId());
        option.setName(entity.getOrgName());
        return option;
    }


    public List<SelectOption> toOptionList(List<Organization> entities) {
        return entities.stream()
                .map(this::toOption)
                .collect(Collectors.toList());

    }
}
