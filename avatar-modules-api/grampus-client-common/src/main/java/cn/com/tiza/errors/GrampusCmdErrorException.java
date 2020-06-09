package cn.com.tiza.errors;

import cn.com.tiza.web.rest.errors.BadRequestException;

/**
 *
 */
public class GrampusCmdErrorException extends BadRequestException {

    private final GrampusCmdStatus error;
    public GrampusCmdErrorException(GrampusCmdStatus errorCode) {
        super("tstar_error_" + errorCode.getCode());

        this.error = errorCode;
    }

    @Override
    public String getTitle() {
        return error.getDesc();
    }
}
