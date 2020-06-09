package cn.com.tiza.web.rest.errors;

/**
 * 错误状态吗的常量类
 *
 * @author villas
 */
public interface ProtocolErrorConstants extends ErrorConstants {

    String VIN_LENGTH_SEVENTEEN = "vin.length.seventeen";
    String IP_IS_INVALID = "ip.is.invalid";
    String VIN_COLLECT_SIZE_LESS_THREE = "vin.collect.size.less.three";
    String VIN_IS_INVALID = "vin.is.invalid";
    String VIN_IS_NOT_EXIST = "vin.is.not.exist";
    String EXIST_TASK_IS_RUNNING = "exist.task.is.running";
    String SOME_ICCID_IS_NULL = "some.iccid.is.null";
    String ICCID_LEN_ERROR = "iccid.len.error";

}
