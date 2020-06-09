package cn.com.tiza.constant;

import lombok.Getter;

/**
 * @author TZ0836
 * 转发报文导出任务表（FWP_EXPORT_TASK）状态
 */
@Getter
public enum ExportTaskStatusEnum {

    NOT_PROCESS("未执行", 0), PROCESSING("执行中", 1), PROCESS_SUCCESS("执行中", 2), PROCESS_EXCEPTION("执行异常", 3);

    private String msg;

    private Integer value;

    private ExportTaskStatusEnum(String msg, Integer value) {
        this.msg = msg;
        this.value = value;
    }

}
