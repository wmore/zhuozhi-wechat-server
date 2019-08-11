package net.joywise.wechat.server.error;

public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public CommonException(String message) {
        this.message = message;
    }

    public CommonException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
