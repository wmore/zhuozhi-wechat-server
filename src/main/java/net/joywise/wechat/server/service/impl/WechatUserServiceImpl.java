package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.wechat.Oauth2AccessToken;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.dao.WechatUserDao;
import net.joywise.wechat.server.enums.AiLangTypeEnum;
import net.joywise.wechat.server.error.WxError;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.MsgService;
import net.joywise.wechat.server.service.WechatUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MsgService msgService;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId
    @Value("${com.constant.weixin.secret}")
    public String secret;//自己在微信测试平台设置的secret


    private static final int TIME_DIFFERENCE_LOSE = 60; //秒


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
            data.put("lang", AiLangTypeEnum.zh_CN.getCode());

            JSONObject resJson = HttpConnectionUtils.get(WX_URL.URL_GET_USER_INFO, data);

            wechatUser = WechatUser.fromJson(resJson.toString());
            return wechatUser;
        } catch (WxErrorException e) {
            throw e;
        }

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

        try {
            String accessToken = baseAccessTokenService.getToken();
            String url = WX_URL.URL_BATCH_GET_USER_INFO.replace("{accessToken}", accessToken);

            JSONObject postJson = new JSONObject();
            JSONArray userJsonArray = new JSONArray();
            for (String openId : openIdList) {
                JSONObject json = new JSONObject();
                json.put("openid", openId);
                json.put("lang", AiLangTypeEnum.zh_CN.getCode());

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
            return wechatUserList;

        } catch (WxErrorException e) {
            throw e;
        }

    }

    @Override
    public String handleSubscribe(Map<String, String> msgMap) {
        String fromOpenId = msgMap.get("FromUserName");
        checkAndSaveWechatUser(fromOpenId);

        handleScanQrcodeAndReplay(msgMap);
        return null;
    }

    /***
     * 确认微信账号是否存在，如果不存在则保存到数据库里
     *
     * @param openId
     */
    private void checkAndSaveWechatUser(String openId) {
        WechatUser userInfo = getUserInfo(openId);
        Assert.notNull(userInfo, "userinfo shold not null");
        WechatUser userInDB = getUserInfoFromDB(openId);
        if (userInDB != null) {
            userInfo.setId(userInDB.getId());
        }
        saveInDB(userInfo);
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

            if (StringUtils.isEmpty(sceneStr)) {
                return msgService.initTextMessage(toOpenId, fromOpenId, "感谢关注卓智智课堂公众号");
            }

            // qescene 的内容是schoolId_snapshotId
            String[] sceneStrSplit = sceneStr.split("_");
            if (sceneStrSplit.length > 1) {
                long schoolId = Long.parseLong(sceneStrSplit[0]);
                long snapshotId = Long.parseLong(sceneStrSplit[1]);
                CourseTeaching courseTeaching = courseTeachingService.queryBySnapshotId(snapshotId, schoolId);
                return msgService.initNewsMessage(toOpenId, fromOpenId, courseTeaching);
            }
        }
        return msgService.initTextMessage(toOpenId, fromOpenId, "感谢关注卓智智课堂公众号");
    }

    @Override
    public void handleScanQrcodeAndReplay(Map<String, String> msgMap) {
        String fromOpenId = msgMap.get("FromUserName");
        String toOpenId = msgMap.get("ToUserName");

        if (msgMap.containsKey("EventKey") || !StringUtils.isEmpty(msgMap.containsKey("EventKey"))) {
            String eventKey = msgMap.get("EventKey");
            String sceneStr = eventKey;

            //if first subscribe, enventkey is  qrscene_xxxx. if not , eventkey is  xxxx, no include qrscene.
            if (eventKey.startsWith("qrscene")) {
                sceneStr = eventKey.replace("qrscene_", "");
            }

            if (StringUtils.isEmpty(sceneStr)) {
                return;
            }

            // qescene 的内容是schoolId_snapshotId
            String[] sceneStrSplit = sceneStr.split("_");
            if (sceneStrSplit.length > 1) {
                long schoolId = Long.parseLong(sceneStrSplit[0]);
                long snapshotId = Long.parseLong(sceneStrSplit[1]);
                CourseTeaching courseTeaching = courseTeachingService.queryBySnapshotId(snapshotId, schoolId);
                msgService.sendTemplateMessage(fromOpenId, courseTeaching);
            }
        }
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


    @Override
    public Oauth2AccessToken getOauth2AccessTokenByCode(String code) throws WxErrorException {
        String key = CACHE_KEY.OAUTH2_ACCESS_TOKEN_KEY_PREFIX + code;

        if (redisUtil.hasKey(key)) {
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
        String key = CACHE_KEY.OAUTH2_ACCESS_TOKEN_KEY_PREFIX + code;
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            log.warn("the key " + key + " already exists, overwrite it .");
        }
        int expiresIn = tokenJson.getInteger("expires_in");

        redisUtil.set(key, tokenJson.toString(), expiresIn - TIME_DIFFERENCE_LOSE);
    }


}
