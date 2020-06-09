package cn.com.tiza.web.rest.errors;
import cn.com.tiza.errors.*;

/**
 * 自定义异常，返回前台 DefaultInterceptor handleData方法统一处理
 * @author TZ08036
 * */
public class BadRequestException extends ThrowableProblem {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    private final String errFileUrl;

    private final StatusType status;

    /**
     * 同于返回错误code
     * */
    public BadRequestException(String errorCode) {
        status = Status.BAD_REQUEST;
        this.errorCode = errorCode;
        this.errFileUrl = "";
    }

    /**
     * 同于excel导入返回错误信息
     * */
    public BadRequestException(String errorCode,String errFileUrl) {
        status = Status.BAD_REQUEST;
        this.errorCode = errorCode;
        this.errFileUrl = errFileUrl;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrFileUrl() {
        return errFileUrl;
    }

    @Override
    public StatusType getStatus() {
        return status;
    }

    @Override
    public String getTitle() {
        return errorCode;
    }
}
