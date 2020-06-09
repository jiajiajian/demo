package cn.com.tiza.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Hbase查询参数
 *
 * @author TZ0836
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class QueryCommand {

    /**
     * 该值不用赋值 通过字典配置与查询工况和报文得到
     */
    private String tableName;
    private String terminalID;
    private Integer cmdID;
    private Long startTime;
    private Long endTime;
    /**
     * 终端类型（协议类型）
     */
    @JsonIgnore
    String terminalType;
}
