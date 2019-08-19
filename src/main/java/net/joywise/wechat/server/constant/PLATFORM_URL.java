package net.joywise.wechat.server.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Title: WX_URL
 * @Description: 其他平台的url
 * @author: wyue
 * @date: 2019/7/31 13:55
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/31 13:55
 * @company: shopin.net
 * @version: V1.0
 */
public class PLATFORM_URL {


    public static final String URL_HTTP_PREFIX = "http://";

    /***
     * 获取学校机构信息
     */
    public static final String URL_GET_SCHOOL_INFO = "/school/config_wechat/{school_id}";

    public static final String URL_LOGIN_SCHOOL_SMART_CLASS_API = "/smartclassapi/login?username={username}&password={password}";

    public static final String URL_LOGIN_SCHOOL_PASSPORT = "/smartclassapi/login?username={username}&password={password}";

    public static final String URL_LOGIN_SCHOOL_PASSPORT_BY_TOKEN = "/passport/loginByToken?token={token}";

    public static final String URL_LOGOUT_SCHOOL_PASSPORT_BY_TOKEN = "/passport/idsLogout?token={token}";

}
