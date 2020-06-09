package cn.com.tiza.web.vm;


import lombok.Data;

import java.util.Map;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class LockVM   {

	/**
	 * 远程锁车id
	 */
	private Long id ;
	/**
	 * 执行状态：0：失败  1：成功  2：超时  3：执行中  4：离线指令已发送
	 */
	private Integer runState ;
	private String runStateName;
	/**
	 * 执行指令---对应数据库字典的id
	 */
	private Long dicItemId ;
	private String itemName;
	private String itemCode;

	/**
	 * 执行人
	 */
	private String loginName ;
	/**
	 * 添加到该表的用户的组织id
	 */
	private Long orgId ;
	/**
	 * 执行时间
	 */
	private Long time ;
	private String executeTime;
	/**
	 * 机器序列号
	 */
	private String vin ;
	/**
	 * check_id
	 */
	private String checkId ;

	/*******非数据库字段*******/
	/**
	 * 终端编码
	 */
	private String terminalCode;
	/**
	 * SIM卡号
	 */
	private String simCode;
	/**
	 * 锁车状态
	 */
	private Integer lock;
	private String lockName;
	private Integer oneLevelLock ;
	private Integer twoLevelLock ;
	private Integer threeLevelLock ;
	/**
	 * ACC状态
	 */
	private Integer acc;
	private String accName;
	/**
	 * 定位状态
	 */
	private Integer gps;
	private String gpsName;
	/**
	 * 数据响应时间
	 */
	private Long dataUpdateTime;
	private String responseTime;
	/**
	 * 组织名称
	 */
	private String orgName;
	/**
	 * 车辆类型
	 */
	private String typeName;
	/**
	 * 车辆型号
	 */
	private String modelName;
	/**
	 * 销售状态
	 */
	private Integer saleStatus;
	private String saleStatusName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 客户电话
	 */
	private String phoneNumber;
	/**
	 * 终端状态
	 */
	private String terminalStatus;

	public LockVM() {
	}


}
