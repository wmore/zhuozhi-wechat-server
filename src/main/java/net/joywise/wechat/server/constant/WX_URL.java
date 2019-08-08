package net.joywise.wechat.server.constant;

/**
 * @Title: WX_URL
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/31 13:55
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/31 13:55
 * @company: shopin.net
 * @version: V1.0
 */
public class WX_URL {

    /***
     * 获取普通access_token
     */
    public static final String URL_GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";

    /***
     * 请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）
     */
    public static final String URL_GET_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={access_token}&type={type}";

    /***
     * 请求获得用户信息
     */
    public static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={access_token}&openid={openid}&lang={lang}";

    /***
     * 批量请求获得用户信息
     */
    public static final String URL_BATCH_GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token={accessToken}";

    /***
     * 获取二维码
     */
    public static final String URL_GET_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={accessToken}";

    /***
     * oauth2网页认证 获取token
     */
    public static final  String URL_OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";

    /***
     * 发送模板消息
     */
    public static final String URL_SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={accessToken}";
}
