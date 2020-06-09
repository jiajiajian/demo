package cn.com.tiza.constant;

import lombok.Getter;

@Getter
public enum ApplyStateEnum {
    no_approved(0), approving(1), success(2), fail(3);

    private Integer state;

    ApplyStateEnum(Integer state) {
        this.state = state;
    }

}
