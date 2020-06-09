package cn.com.tiza.constant;

import lombok.Getter;

@Getter
public enum ActionEnum {
    start("发起",0),
    approving("同意",1),
    send_back("退回",2),
    withdraw("撤回",3),
    turn_to_send("转派",4),
    restart("重新申请",5),
    end("结束申请",6);

    private String name;

    private Integer action;

    ActionEnum(String name,Integer action)
    {
        this.name = name;
        this.action = action;
    }

    public static String getType(int desc) {
        ActionEnum[] carTypeEnums = values();
        for (ActionEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getAction() == desc) {
                return carTypeEnum.getName();
            }
        }
        return null;
    }
}
