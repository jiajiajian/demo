package cn.com.tiza.constant;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * 通用参数枚举类
 *
 * @author villas
 */
@Getter
public enum GeneralParamEnum {

    /**
     * 主中心IP地址
     */
    HOST_IP(4, 0x0201),

    /**
     * 副中心IP
     */
    DEPUTY_IP(4, 0x0203),

    /**
     * 中心端口号
     */
    PORT(2, 0x0205),

    /**
     * ACC累计时间
     */
    ACC_SUM_TIME(4, 0x0304),

    /**
     * 是否启用休眠模式
     */
    IS_SLEEP(1, 0x0309),

    /**
     * ACC开关巡检间隔
     */
    ACC_OPEN_CLOSE(4, 0X020B),
    /**
     * reset恢复出厂设置
     */
    RESET(0, 0x0302),
    /**
     * 软件版本
     */
    SOFTWARE_VERSION(32, 0x0303),

    /**
     * VIN码
     */
    VIN_CODE(17,0x0901);

    /**
     * 字节长度
     */
    private int len;

    /**
     * 参数id占两个字节
     */
    private int id;

    GeneralParamEnum(int len, int id) {
        this.len = len;
        this.id = id;
    }

    public byte[] getBytes(byte[] value) {
        Optional<Integer> bodyLen = Optional.ofNullable(value).map(b -> b.length);
        ByteBuffer buffer = ByteBuffer.allocate(2 + bodyLen.orElse(0));
        buffer.putShort((short) this.id);
        bodyLen.ifPresent(t -> buffer.put(value));
        return buffer.array();
    }

    public static Optional<GeneralParamEnum> fromId(int id) {
        for (GeneralParamEnum type : GeneralParamEnum.values()) {
            if (type.id == id) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
