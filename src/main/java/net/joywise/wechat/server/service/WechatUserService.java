package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.wechat.Oauth2AccessToken;
import net.joywise.wechat.server.error.WxErrorException;

import java.util.List;
import java.util.Map;

public interface WechatUserService {

    void saveInDB(WechatUser wechatUser);

    WechatUser getUserInfoFromDB(String openId);

    List<WechatUser> getAllFromDB();

    /***
     * 获取单个微信用户信息
     * @param openId
     * @return
     */
    WechatUser getUserInfo(String openId);

    /***
     * 批量获取微信用户信息
     * @param openIdList
     * @return
     */
    List<WechatUser> batchGetUserInfo(List<String> openIdList);

    /***
     * 关注事件处理
     * @param msgMap
     */
    String handleSubscribe(Map<String, String> msgMap);

    /***
     * 扫码事件
     * @param msgMap
     * @return
     */
    String handleScanQrcode(Map<String, String> msgMap);

    /***
     * 扫码事件，并回复模板消息
     * @param msgMap
     * @return
     */
     void handleScanQrcodeAndReplay(Map<String, String> msgMap);

    /***
     * 取消关注事件处理
     * @param msgMap
     */
    boolean handleUnSubscribe(Map<String, String> msgMap);


    /***
     * 通过code换取网页授权access_token
     * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
     * 公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，
     * 也获取到了openid，snsapi_base式的网页授权流程即到此为止。
     * @param code
     * @param appId
     * @param secret
     * @return
     */
    Oauth2AccessToken getOauth2AccessTokenByCode(String code) throws WxErrorException;

}
