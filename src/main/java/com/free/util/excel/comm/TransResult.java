package com.free.util.excel.comm;

import java.util.List;

public class TransResult<T> {

    private String msg;
    private Boolean success;
    private ErrorType errorType;
    private List<ConvertResult<T>> entities;
    private String errorFileName;

    public TransResult() {
        this.success = true;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public List<ConvertResult<T>> getEntities() {
        return entities;
    }

    public void setEntities(List<ConvertResult<T>> entities) {
        this.entities = entities;
    }

    public String getErrorFileName() {
        return errorFileName;
    }

    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName;
    }

}
