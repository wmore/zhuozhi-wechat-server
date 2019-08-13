package net.joywise.wechat.server.error;

import lombok.Data;
import net.joywise.wechat.server.enums.ErrorCode;

@Data
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode iErrorCode;

    private String code;

    public String message;

    public CommonException(String message) {
        this.message = message;
    }


    public CommonException(ErrorCode errorCode) {
        this.message = errorCode.getErrorMessage();
        this.code = errorCode.getErrorCode();
    }

    public CommonException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

}
