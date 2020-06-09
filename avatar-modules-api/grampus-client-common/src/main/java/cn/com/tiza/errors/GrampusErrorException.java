package cn.com.tiza.errors;

import cn.com.tiza.web.rest.errors.BadRequestException;

/**
 * TStar查询异常
 * @author tiza
 */
public class GrampusErrorException extends BadRequestException {

    private final GrampusErrorCode error;
    public GrampusErrorException(GrampusErrorCode errorCode) {
        super("tstar_error_" + errorCode.getCode());

        this.error = errorCode;
    }

    @Override
    public String getTitle() {
        return error.getDesc();
    }
}
