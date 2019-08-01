package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.wechat.Oauth2AccessToken;
import net.joywise.wechat.server.bean.wechat.message.News;
import net.joywise.wechat.server.bean.wechat.message.NewsMessage;
import net.joywise.wechat.server.bean.wechat.message.TextMessage;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.dao.WechatUserDao;
import net.joywise.wechat.server.enums.AiLangType;
import net.joywise.wechat.server.error.WxError;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.WechatUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import net.joywise.wechat.server.util.WeixinMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class WechatUserServiceImpl implements WechatUserService {

    @Autowired
    private WechatUserDao wechatUserDao;

    @Autowired
    private BaseAccessTokenService baseAccessTokenService;

    @Autowired
    private CourseTeachingService courseTeachingService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId
    @Value("${com.constant.weixin.secret}")
    public String secret;//自己在微信测试平台设置的secret


    @Override
    public WechatUser getUserInfoFromDB(String openId) {
        WechatUser user = wechatUserDao.queryByOpenId(openId);
        return user;
    }

    @Override
    public void saveInDB(WechatUser wechatUser) {
        wechatUserDao.save(wechatUser);
    }

    @Override
    public List<WechatUser> getAllFromDB() {
        return (List<WechatUser>) wechatUserDao.findAll();
    }

    @Override
    public WechatUser getUserInfo(String openId) {
        /***
         * 微信接口返回值：
         * {
         *     "subscribe": 1,
         *     "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M",
         *     "nickname": "Band",
         *     "sex": 1,
         *     "language": "zh_CN",
         *     "city": "广州",
         *     "province": "广东",
         *     "country": "中国",
         *     "headimgurl":"http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
         *     "subscribe_time": 1382694957,
         *     "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
         *     "remark": "",
         *     "groupid": 0,
         *     "tagid_list":[128,2],
         *     "subscribe_scene": "ADD_SCENE_QR_CODE",
         *     "qr_scene": 98765,
         *     "qr_scene_str": ""
         * }
         */
        WechatUser wechatUser = null;
        String accessToken = null;
        try {
            accessToken = baseAccessTokenService.getToken();
            Map<String, Object> data = new HashMap<>();
            data.put("access_token", accessToken);
            data.put("openid", openId);
            data.put("lang", AiLangType.zh_CN.getCode());

            JSONObject resJson = HttpConnectionUtils.get(WX_URL.URL_GET_USER_INFO, data);

            wechatUser = WechatUser.fromJson(resJson.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }

        return wechatUser;

    }

    @Override
    public List<WechatUser> batchGetUserInfo(List<String> openIdList) {
        /***
         * 微信接口
         * POST数据示例
         *
         * {
         *     "user_list": [
         *         {
         *             "openid": "otvxTs4dckWG7imySrJd6jSi0CWE",
         *             "lang": "zh_CN"
         *         },
         *         {
         *             "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg",
         *             "lang": "zh_CN"
         *         }
         *     ]
         * }
         * 返回值：
         * {
         *    "user_info_list": [
         *        {
         *            "subscribe": 1,
         *            "openid": "otvxTs4dckWG7imySrJd6jSi0CWE",
         *            "nickname": "iWithery",
         *            "sex": 1,
         *            "language": "zh_CN",
         *            "city": "揭阳",
         *            "province": "广东",
         *            "country": "中国",
         *
         *            "headimgurl": "http://thirdwx.qlogo.cn/mmopen/xbIQx1GRqdvyqkMMhEaGOX802l1CyqMJNgUzKP8MeAeHFicRDSnZH7FY4XB7p8XHXIf6uJA2SCunTPicGKezDC4saKISzRj3nz/0",
         *
         *           "subscribe_time": 1434093047,
         *            "unionid": "oR5GjjgEhCMJFyzaVZdrxZ2zRRF4",
         *            "remark": "",
         *
         *            "groupid": 0,
         *            "tagid_list":[128,2],
         *            "subscribe_scene": "ADD_SCENE_QR_CODE",
         *            "qr_scene": 98765,
         *            "qr_scene_str": ""
         *
         *       },
         *        {
         *            "subscribe": 0,
         *            "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg"
         *        }
         *    ]
         * }
         */
        List<WechatUser> wechatUserList = new ArrayList<>();

        String accessToken = null;
        try {
            accessToken = baseAccessTokenService.getToken();
            String url = WX_URL.URL_BATCH_GET_USER_INFO.replace("{accessToken}", accessToken);

            JSONObject postJson = new JSONObject();
            JSONArray userJsonArray = new JSONArray();
            for (String openId : openIdList) {
                JSONObject json = new JSONObject();
                json.put("openid", openId);
                json.put("lang", AiLangType.zh_CN.getCode());

                userJsonArray.add(json);
            }
            postJson.put("user_list", userJsonArray);

            JSONObject resJson = HttpConnectionUtils.post(url, postJson);

            JSONArray json = resJson.getJSONArray("user_info_list");

            if (json.size() > 0) {
                for (int i = 0; i < json.size(); i++) {
                    JSONObject job = json.getJSONObject(i);
                    WechatUser user = WechatUser.fromJson(resJson.toString());
                    wechatUserList.add(user);
                }
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }

        return wechatUserList;
    }

    @Override
    public String handleSubscribe(Map<String, String> msgMap) {
        String fromOpenId = msgMap.get("FromUserName");
        WechatUser userInfo = getUserInfo(fromOpenId);
        Assert.notNull(userInfo, "userinfo shold not null");
        WechatUser userInDB = getUserInfoFromDB(fromOpenId);
        if (userInDB != null) {
            userInfo.setId(userInDB.getId());
        }
        saveInDB(userInfo);

        return handleScanQrcode(msgMap);
    }

    @Override
    public String handleScanQrcode(Map<String, String> msgMap) {
        String fromOpenId = msgMap.get("FromUserName");
        String toOpenId = msgMap.get("ToUserName");

        if (msgMap.containsKey("EventKey") || !StringUtils.isEmpty(msgMap.containsKey("EventKey"))) {
            String eventKey = msgMap.get("EventKey");
            String sceneStr = eventKey;

            //if first subscribe, enventkey is  qrscene_xxxx. if not , eventkey is  xxxx, no include qrscene.
            if (eventKey.startsWith("qrscene")) {
                sceneStr = eventKey.replace("qrscene_", "");
            }

            if (!StringUtils.isEmpty(sceneStr) && sceneStr.split("_").length > 1) {
                long schoolId = Long.parseLong(sceneStr.split("_")[0]);
                long snapshotId = Long.parseLong(sceneStr.split("_")[1]);
                CourseTeaching courseTeaching = courseTeachingService.queryBySnapshotId(snapshotId, schoolId);
                return initNewsMessage(toOpenId, fromOpenId, courseTeaching);
            }
        }

        return initTextMessage(toOpenId, fromOpenId, "感谢关注卓智智课堂公众号");

    }

    @Override
    @Transactional
    public boolean handleUnSubscribe(Map<String, String> msgMap) {
        /***
         * 取消关注，不会删除 smart 用户的绑定关系
         */
        String openId = msgMap.get("FromUserName");
        WechatUser user = getUserInfoFromDB(openId);
        if (user != null) {
            user.setSubscribe(false);
            user.setUnSubscribeTime(String.valueOf(System.currentTimeMillis()));
            saveInDB(user);
        }
        return true;
    }

    private static final String OAUTH2_ACCESS_TOKEN_KEY = "wechat:accessToken:";
    private static final int TIME_DIFFERENCE_LOSE = 60; //秒

    @Override
    public Oauth2AccessToken getOauth2AccessTokenByCode(String code) throws WxErrorException {
        String key = OAUTH2_ACCESS_TOKEN_KEY + code;

        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            return Oauth2AccessToken.fromJson((String) redisUtil.get(key));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("appid", appId);
        data.put("secret", secret);

        JSONObject resJson = HttpConnectionUtils.get(WX_URL.URL_OAUTH2_ACCESS_TOKEN, data);

        WxError error = WxError.fromJson(resJson);
        if (error.getErrCode() != 0) {
            throw new WxErrorException(error);
        }

        saveOauth2AccessToken(code, resJson);
        return Oauth2AccessToken.fromJson(resJson.toString());
    }


    private void saveOauth2AccessToken(String code, JSONObject tokenJson) {
        String key = OAUTH2_ACCESS_TOKEN_KEY + code;
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            log.warn("the key " + key + " already exists, overwrite it .");
        }
        int expiresIn = tokenJson.getInteger("expires_in");

        redisUtil.set(key, tokenJson.toString(), expiresIn - TIME_DIFFERENCE_LOSE);
    }

    /***
     * 新建文本消息
     * @param fromUserName
     * @param toUserName
     * @param respContent
     * @return
     */
    private String initTextMessage(String fromUserName, String toUserName, String respContent) {
        // 默认回复文本消息
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(WeixinMessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent(respContent);
        return WeixinMessageUtil.textMessageToXml(textMessage);
    }

    /***
     * 新建图文消息
     * @param toUserName
     * @param fromUserName
     * @param courseTeaching
     * @return
     */
    private String initNewsMessage(String toUserName, String fromUserName, CourseTeaching courseTeaching) {
        List<News> newsList = new ArrayList<>();
        //图文消息实体
        NewsMessage newsMessage = new NewsMessage();
        //图文消息的内容实体
        News news = new News();
        news.setTitle(courseTeaching.getCourseName());
        news.setDescription(courseTeaching.getCourseName() + "二维条码/二维码（2-dimensional bar code）是用某种特定的几何图形按一定规律在平面（二维方向上）分布的黑白相间的图形记录数据符号信息的；在代码编制上巧妙地利用构成计算机内部逻辑基础的“0”、“1”比特流的概念，使用若干个与二进制相对应的几何形体来表示文字数值信息，通过图象输入设备或光电扫描设备自动识读以实现信息自动处理：它具有条码技术的一些共性：每种码制有其特定的字符集；每个字符占有一定的宽度；具有一定的校验功能等。同时还具有对不同行的信息自动识别功能、及处理图形旋转变化点。");
        news.setPicUrl(courseTeaching.getIndexImgUrl());//需要替换本地服务器图片文件
        news.setUrl(courseTeaching.getLessonUrl());
        newsList.add(news);

        newsMessage.setFromUserName(toUserName);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setMsgType(WeixinMessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        String message = WeixinMessageUtil.newsMessageToXml(newsMessage);
//        log.debug(message);
        return message;
    }
}
