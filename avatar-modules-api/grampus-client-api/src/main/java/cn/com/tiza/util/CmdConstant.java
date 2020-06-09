package cn.com.tiza.util;

/**
 * 指令值
 * 10-->7d
 * 11-->78
 * 02-->72
 * 18-->7f
 * 40-->84
 *
 * @author villas
 */
public interface CmdConstant {

    /**
     * 继电器锁车/解锁
     */
    int RELAY_LOCK = 0x48;

    /**
     * 控制器锁车/解锁
     */
    int CONTROLLER_LOCK = 0x40;

    /**
     * 工况数据指令/工作参数查询
     */
    int TOTAL_DATA = 0x18;

    /**
     * 定位指令
     */
    int LOCATION = 0x02;

    /**
     * 通用参数设置
     */
    int GENERAL_PARAM_SET = 0x04;
    /**
     * 参数查询指令
     */
    int PARAM_QUERY = 0x11;
    /**
     * 工作参数上传设置(ACC)
     */
    int WORK_PARAM_SET = 0x2c;
    /**
     * 解除报警
     */
    int RELEASE_ALARM = 0x05;

    /**
     * remote upgrade
     */
    int UPGRADE = 0x10;

    int TRACE = 0x03;

}
