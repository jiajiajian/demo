package cn.com.tiza.tstar.common.utils;




public class DataConverter
{
    public static int byte2int(byte[] b) {
        int l = 0;
        for (int i = 0; i < b.length; i++) {
            l += (int)((b[i] & 0xFF) * Math.pow(256.0D, (b.length - i - 1)));
        }
        return l;
    }

    public static long byte2long(byte[] b) {
        long l = 0L;
        for (int i = 0; i < b.length; i++) {
            l += (long)((b[i] & 0xFF) * Math.pow(256.0D, (b.length - i - 1)));
        }
        return l;
    }



    public static String byte2Hex(byte value) { return String.format("%02X", new Object[] { Byte.valueOf(value) }); }



    public static String bcd2Hex(byte[] bcd) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bcd.length; i++) {
            buf.append(byte2Hex(bcd[i]));
        }
        return buf.toString();
    }


    public static String fillWithZero(String value, int length) {
        String result = value;
        while (result.length() < length) {
            result = "0" + result;
        }
        return result;
    }


    public static byte[] hex2BCD(String value) {
        byte[] result = new byte[value.length() / 2];
        int idx = 0;
        for (int i = 0; i < result.length; i++) {
            idx = i * 2;
            result[i] = Byte.parseByte(value.substring(idx, idx + 2), 16);
        }
        return result;
    }


    public static String bytes2Hex(byte[] value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            sb.append(String.format("%02X", new Object[] { Byte.valueOf(value[i]) }));
        }
        return sb.toString();
    }


    public static String bytes2HexFormat(byte[] value) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < value.length; i++) {
            sb.append(String.format("%02X", new Object[] { Byte.valueOf(value[i]) })).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }









    public static String bytesToMinaString(byte[] data) {
        StringBuilder buf = new StringBuilder();
        buf.append("[pos=0").append(" lim=")
                .append(data.length).append(" cap=")
                .append(data.length).append(": ");
        for (int i = 0; i < data.length; i++) {
            buf.append(String.format("%02X", new Object[] { Byte.valueOf(data[i]) }));
            buf.append(" ");
        }
        buf.append(']');
        return buf.toString();
    }


    public static byte[] hexStr2Bytes(String str) {
        str = str.replaceAll(" ", "");
        if (str.length() % 2 != 0) {
            return null;
        }
        byte[] bts = new byte[str.length() / 2];
        for (int i = 0, j = 0; i < str.length() - 1; i += 2, j++) {
            bts[j] = (byte)Short.parseShort(str.substring(i, i + 2), 16);
        }
        return bts;
    }
}