package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class FenceDto {
    /**
     * 名称
     */
    @NotNull
    private String name;
    /**
     * 组织
     */
    private Long organizationId;
    /**
     * 0:出围栏报警 1:进围栏报警
     */
    @NotNull
    private Integer alarmType;
    /**
     * 类型 1：图形围栏 2：行政围栏 3:时间围栏
     */
    @NotNull
    private Integer fenceType;
    /**
     * 关联车辆数
     */
    private int vehicleNum;
    /**
     * 区域信息
     */
    @NotNull
    private String area;
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;

    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;

    public FenceDto() {
    }


}
