package cn.com.tiza.util;

import cn.com.tiza.dto.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: TZ0781
 **/
public class TreeUtil {

    /**
     * 使用递归方法建roletree
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> List<T> buildTreeByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * @return
     * @Author jiajian
     * @Description 递归权限树
     * @Date 13:46 2019/2/25
     * @Param
     **/
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getKey().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }

            if (treeNode.getChildren().size() == 0) {
                treeNode.setIsLeaf(true);
            } else {
                treeNode.setIsLeaf(false);
            }
        }
        return treeNode;
    }

}
