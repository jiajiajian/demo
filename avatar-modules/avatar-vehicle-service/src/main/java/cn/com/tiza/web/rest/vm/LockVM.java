package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.Map;

/**
 * @author tz0920
 */
@Data
public class LockVM {
    private Integer lock;
    private Integer oneLevelLock;
    private Integer twoLevelLock;
    private Integer threeLevelLock;
    private Map<String, Integer> lockStatus;
}
