package cn.com.tiza.constant;

/**
 * 数据来源类型
 *
 * @author villas
 */
public enum DatasourceType {
    /**
     * 数据来源于kafka
     */
    KAFKA("kafka"),
    /**
     * 来源socket
     */
    SOCKET("socket"),
    /**
     * null
     */
    NULL("null");

    private String type;

    DatasourceType(String type) {
        this.type = type;
    }

    public static DatasourceType matchType(String str) {
        for (DatasourceType dt : DatasourceType.values()) {
            if (dt.name().equalsIgnoreCase(str)) {
                return dt;
            }
        }
        return DatasourceType.NULL;
    }
}
