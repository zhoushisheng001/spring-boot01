package com.zhuguang.zhou.export;

import com.zhuguang.zhou.pojo.AppCode;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private AppCode appCode;

    public ApplicationException() {
    }

    public ApplicationException(AppCode appCode) {
        super(appCode.getMessage());
        this.appCode = appCode;
    }

    public ApplicationException(AppCode appCode, Object... msgArgs) {
        super(String.format(appCode.getMessage(), msgArgs));
        this.appCode = appCode;
    }

    public ApplicationException(int code, String message) {
        this(code, message, (Throwable)null);
    }

    public ApplicationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.appCode = new ApplicationException.DefaultAppCode(code, message);
    }

    public AppCode getAppCode() {
        return this.appCode;
    }

    public void setAppCode(AppCode appCode) {
        this.appCode = appCode;
    }

    class DefaultAppCode implements AppCode {
        private int code;
        private String message;

        public DefaultAppCode(int code, String message) {
            this.setCode(code);
            this.setMessage(message);
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
