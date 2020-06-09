package cn.com.tiza.errors;

public enum GrampusCmdStatus {

	/**
	 *
	 */
	S_1(-1, "未知状态"),
	S0(0, "响应成功（最终状态）"),
	S1(1, "网关接收指令成功"),
	S2(2, "终端接收指令成功"),
	S3(3, "终端已从网关断开连接（实时指令的最终状态）"),
	S4(4, "网关已缓存指令"),
	S5(5, "指令待发送"),
	S6(6, "没有可用的网关（最终状态）");
	
	private int code;
	
	private String desc;

	GrampusCmdStatus(int code, String desc) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static GrampusCmdStatus valueOf(int status) {
		for(GrampusCmdStatus sta: GrampusCmdStatus.values()) {
			if(sta.getCode() == status) {
				return sta;
			}
		}
		return S_1;
	}
}
