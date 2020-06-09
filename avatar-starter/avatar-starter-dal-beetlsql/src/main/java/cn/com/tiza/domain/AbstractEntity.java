package cn.com.tiza.domain;


import cn.com.tiza.context.BaseContextHandler;
import org.beetl.sql.core.annotatoin.UpdateIgnore;

/**
 * @author tiza
 */
public abstract class AbstractEntity {

    private Long createTime;
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;

    private Long updateTime;
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;

    public AbstractEntity() {
    }

    @UpdateIgnore
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @UpdateIgnore
    public String getCreateUserAccount() {
        return createUserAccount;
    }

    public void setCreateUserAccount(String createUserAccount) {
        this.createUserAccount = createUserAccount;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserAccount() {
        return updateUserAccount;
    }

    public void setUpdateUserAccount(String updateUserAccount) {
        this.updateUserAccount = updateUserAccount;
    }

    @UpdateIgnore
    public String getCreateUserRealname() {
        return createUserRealname;
    }

    public void setCreateUserRealname(String createUserRealname) {
        this.createUserRealname = createUserRealname;
    }

    public String getUpdateUserRealname() {
        return updateUserRealname;
    }

    public void setUpdateUserRealname(String updateUserRealname) {
        this.updateUserRealname = updateUserRealname;
    }

    public void setCreatorInfo() {
        this.createTime = System.currentTimeMillis();
        this.setCreateUserAccount(BaseContextHandler.getLoginName());
        this.setCreateUserRealname(BaseContextHandler.getName());
        setUpdateInfo();
    }

    public void setUpdateInfo() {
        this.setUpdateTime(System.currentTimeMillis());
        this.setUpdateUserAccount(BaseContextHandler.getLoginName());
        this.setUpdateUserRealname(BaseContextHandler.getName());
    }
}
