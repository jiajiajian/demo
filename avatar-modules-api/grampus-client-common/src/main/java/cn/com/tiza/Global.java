package cn.com.tiza;

/**
 * @author tiza
 */
public final class Global {

    public static final String HEADER_API_KEY = "T-ApiKey";

    /**
     *
     */
    private static final String DATA_TABLE_PREFIX = "tiza_";

    /**
     *
     */
    private static final String DATA_TABLE_RAW = "_rawdata";
    /**
     *
     */
    private static final String DATA_TABLE_TRACK = "_trackdata";

    /**
     * HBase原始数据表
     *
     * @param terminalType
     * @return
     */
    public static String rawTableName(String terminalType) {
        return DATA_TABLE_PREFIX + terminalType + DATA_TABLE_RAW;
    }

    /**
     * HBase历史数据表
     *
     * @param terminalType
     * @return
     */
    public static String trackTableName(String terminalType) {
        return DATA_TABLE_PREFIX + terminalType + DATA_TABLE_TRACK;
    }

    /**
     * HBase转发数据表
     *
     * @param taskId
     * @return
     */
    public static String fwpTableName(String terminalType, Long taskId) {
        return DATA_TABLE_PREFIX + terminalType + "_" + taskId.toString() + "_forwarddata";
    }

    public static String assemblePrefix(String terminalType) {
        return DATA_TABLE_PREFIX + terminalType;
    }
}
