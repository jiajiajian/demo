package cn.com.tiza.dto;

public class RestResult {

    private Boolean isSuccess;

    private Integer errorCode;

    public RestResult() {

    }

    public RestResult(Boolean isSuccess, Integer errorCode) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
