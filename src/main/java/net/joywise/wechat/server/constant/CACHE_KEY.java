package net.joywise.wechat.server.constant;

/**
 * @Title: CACHE_KEY
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/5 14:49
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/5 14:49
 * @company: shopin.net
 * @version: V1.0
 */
public class CACHE_KEY {

    /***
     * 存储 公众号的全局access_token
     */
    public static final String ACCESS_TOKEN_KEY = "h5:wechat:accessToken";

    /***
     * key前缀，存储 公众号jpi的ticket
     */
    public static final String TICKET_KEY_PREFIX = "h5:wechat:ticket:";

    /***
     * key前缀，存储 微信号oauth2的token
     */
    public static final String OAUTH2_ACCESS_TOKEN_KEY_PREFIX = "h5:wechat:accessToken:";

    /***
     * 可以前缀，h5:smart:token:{openid}， 存储账号登陆学校平台的smart token
     */
    public static final String USER_SMART_TOKEN_KEY_PREFIX = "h5:smart:token:";

    /***
     * 可以前缀，h5:smart:userinfo:{openid}， 缓存用户信息
     */
    public static final String SMART_USER_INFO_KEY_PREFIX = "h5:smart:userinfo:";


}
