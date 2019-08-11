package net.joywise.wechat.server.constant;

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

    public static final String DOMAIN_CLOUD_BASE =  "http://localhost:8080/ROOT/";
    /***
     * 获取学校机构信息
     */
    public static final String URL_GET_SCHOOL_INFO = "school/{school_id}";

    public static final String URL_LOGIN_SCHOOL_PASSPORT = "passport/login";

    public static final String URL_LOGIN_SCHOOL_PASSPORT_BY_TOKEN = "passport/loginByToken";


}
