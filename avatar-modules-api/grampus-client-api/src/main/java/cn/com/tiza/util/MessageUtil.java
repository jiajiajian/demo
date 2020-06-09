package cn.com.tiza.util;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * 锁车解锁报文工具类
 *
 * @author villas
 */
@Slf4j
public class MessageUtil {

    /**
     * 组装命令报文
     *
     * @param sim     sim卡号
     * @param version 协议版本号
     * @param cmd     命令id
     * @param body    报文body
     * @return base64编码之后报文
     */
    public static String packCmdBody(String sim, int version, int cmd, int serialNo, String body) {
        Objects.requireNonNull(sim, "sim card num can't null");
        byte[] bytes = Optional.ofNullable(body).map(MessageUtil::hexStr2Bytes).orElse(null);
        return packCmdBody(sim, version, cmd, serialNo, bytes);
    }

    /**
     * 组装命令报文
     *
     * @param sim     sim卡号
     * @param version 协议版本号
     * @param cmd     命令id
     * @param body    body报文字节数组
     * @return base64编码之后报文
     */
    public static String packCmdBody(String sim, int version, int cmd, int serialNo, byte[] body) {
        byte[] simBytes = new byte[6];
        if (version == 0) {
            byte[] bytes = longToBytes(Long.parseLong(sim.substring(1)));
            System.arraycopy(bytes, 0, simBytes, 0, bytes.length);
            simBytes[simBytes.length - 1] = (byte) 2;
        } else {
            simBytes = hexStr2Bytes(leftFillZero(Long.toHexString(Long.parseLong(sim)), 12));
            Objects.requireNonNull(simBytes, "sim byte is null");
        }
        return Base64.getEncoder().encodeToString(getMessage(simBytes, cmd, serialNo, body));
    }


    private static byte[] getMessage(byte[] sim, int cmd, int serialNo, byte[] body) {
        Optional<Integer> bodyLen = Optional.ofNullable(body).map(b -> b.length);
        // 消息的总长度(字节数2) ; 命令序号长度为2; 命令id长度都为1
        short len = (short) (2 + sim.length + 2 + 1 + bodyLen.orElse(0));
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.putShort(len);
        byteBuffer.put(sim);
        byteBuffer.putShort((short) serialNo);
        byteBuffer.put((byte) cmd);
        bodyLen.ifPresent(t -> byteBuffer.put(body));
        return byteBuffer.array();
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }


    private MessageUtil() {
    }

    /**
     * 获取数组的子数组
     *
     * @param src   源数组
     * @param begin 从源数组的开始位置
     * @param count 子数组的长度
     * @return 子数组
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        if (begin < 0 || begin > (src.length - 1) || (begin + count) > src.length) {
            throw new ArrayIndexOutOfBoundsException("begin(" + begin + ") or count(" + count + ") out of bound src");
        }
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * 获取无符号byte值
     */
    public static int getUnsignedByte(byte b) {
        return b & 0xFF;
    }

    /**
     * 获取无符号short值
     */
    public static int getUnsignedShort(byte[] bytes) {
        return Shorts.fromByteArray(bytes) & 0xFFFF;
    }

    /**
     * 获取无符号int值
     */
    public static long getUnsignedInt(byte[] bytes) {
        return Ints.fromByteArray(bytes) & 0xFFFFFFFFL;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] hexStr2Bytes(String str) {
        str = str.replace(" ", "");
        if (str.length() % 2 != 0) {
            throw new NumberFormatException(str + "'s length" + str.length() + " is invalid");
        } else {
            byte[] bts = new byte[str.length() / 2];
            int i = 0;

            for (int j = 0; i < str.length() - 1; ++j) {
                bts[j] = (byte) Short.parseShort(str.substring(i, i + 2), 16);
                i += 2;
            }

            return bts;
        }
    }


    private static String leftFillZero(String str, int len) {
        Objects.requireNonNull(str, "str can't null");
        int i = len - str.length();
        if (i <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append("0");
        }
        return sb.append(str).toString();
    }

    public static void main(String[] args) {
        String str = "AA8ABCjI/dUADHEAUhAA";
        byte[] decode = Base64.getDecoder().decode(str);
        System.out.println(bytesToHex(decode));
    }

}
