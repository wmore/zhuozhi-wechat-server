package net.joywise.wechat.server.enums;

/**
 * @Title: SmartUserErrorEnum
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/12 21:25
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/12 21:25
 * @company: shopin.net
 * @version: V1.0
 */
public enum SmartUserErrorEnum implements ErrorCode {

    SCHOOL_INFO_NOT_FOUND("1001", "查不到学校的信息!"),
    PASSPORT_LOGIN_FAILED("1002", "登陆失败，用户名或密码错误！"),
    USER_NOT_FOUND("1003", "该用户不存在");

    private String errorCode;
    private String errorMessage;

    SmartUserErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
