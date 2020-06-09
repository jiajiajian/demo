package cn.com.tiza.errors;

/**
 * 指令发送错误
 * @author tiza
 */
public enum GrampusErrorCode {

	/**
	 * 未知错误
	 */
	E0000(0, "未知错误"),
	E1000(1000, "ApiKey鉴权失败"),
	E1001(1001, "参数格式非法（必需参数为空）"),
	E1002(1002, "终端类型无效"),
	E1003(1003, "指令类型非法"),
	E1004(1004, "终端不存在"),
	E1005(1005, "终端已下线"),
	E1006(1006, "等待时间格式有误"),
	E1007(1007, "没有可用的网关"),

	//E1101(1101, "执行时间格式有误"),
	//E1102(1102, "执行时间小于当前时间"),

	E1101(1101, "请求参数格式非法，请求体为空；"),
	E1102(1102, "终端类型无效"),
	E1103(1103, "终端已存在"),

	E1201(1201, "参数格式非法，必须参数为空"),
	E1202(1202, "终端类型无效"),

	E1301(1301, "参数格式非法（必需参数为空，pageNo小于1，pageSize小于1）"),
	E1302(1302, "终端类型无效"),

	E1401(1401, "参数格式非法（必需参数为空）"),
	E1402(1402, "终端类型无效"),
	E1403(1403, "终端不存在"),

	E1501(1501, "参数格式非法（必需参数为空）"),
	E1502(1502, "HBase表不存在或无效表名或无权限"),

	E1601(1601, "参数格式非法（必需参数为空，rows<1）"),
	E1602(1602, "查询器ID不存在或已超时移除"),

	E1701(1701, "参数格式非法（必需参数为空）或（queryId<=0）"),

	E1801(1801, "参数格式非法（必需参数为空）"),
	E1802(1802, "终端类型无效"),
	E1803(1803, "终端无数据"),

	E9999(9999, "服务调用失败！"),
	E10000(10000, "查询指令响应超时异常！"),
	;

	private int code;
	
	private String desc;

	GrampusErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return code + desc;
	}

	public String getOnlyDesc() {return desc;}
	
	public static GrampusErrorCode valueOf(int errCode) {
		for(GrampusErrorCode err: GrampusErrorCode.values()) {
			if(err.getCode() == errCode) {
				return err;
			}
		}
		return E0000;
	}
}
