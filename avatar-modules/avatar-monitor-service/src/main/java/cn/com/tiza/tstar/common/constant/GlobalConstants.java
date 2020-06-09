package cn.com.tiza.tstar.common.constant;

import java.nio.charset.Charset;


public class GlobalConstants {
    public static final String GLOBAL_CHARSET_NAME = "utf-8";
    public static final Charset GLOBAL_CHARSET = Charset.forName("utf-8");
    public static final String GLOBAL_TABLENAME_TERMINAL_TYPE = "t_terminaltype";
    public static final String GLOBAL_TOPIC_POSTFIX_RAWDATA = "_rawdata";
    public static final String GLOBAL_TOPIC_POSTFIX_RAWDATA_ILLEGAL = "_rawdata_illegal";
    public static final String GLOBAL_TOPIC_POSTFIX_RAWDATA_DOWN = "_rawdata_down";
    public static final String GLOBAL_TABLENAME_GATEWAYINFO = "t_gateway";
    public static final int HBASE_REGION_NUM = 32768;
    public static final String HBASE_COLUMN_FAMILY = "1";
    public static final String GLOBAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String GLOBAL_TABLENAME_POSTFIX_TERMINALINFO = "_terminal";
}
