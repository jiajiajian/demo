package cn.com.tiza.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiajian
 */
@Getter
@Setter
public class TreeNode {
    protected Long key;
    protected Long parentId;
    protected Boolean isLeaf;
 
    List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node){
        children.add(node);
    }
}