package cn.com.tiza.service.dto;

/**
 * @author
 */
public enum AlarmNoticeType {

    ALARM("ALARM", "报警项"),

    FAULT("FAULT", "故障"),

    FENCE("FENCE", "围栏报警");

    AlarmNoticeType(String label, String value) {
        this.label = label;
        this.value = value;

    }

    private String label;
    private String value;

    public static String fromName(String name) {
        for (AlarmNoticeType type : AlarmNoticeType.values()) {
            if (type.label.equals(name)) {
                return type.value;
            }
        }
        return null;
    }


}
