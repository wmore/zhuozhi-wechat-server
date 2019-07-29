package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.dao.WechatUserRepository;
import net.joywise.wechat.server.enums.AiLangType;
import net.joywise.wechat.server.service.AccessTokenService;
import net.joywise.wechat.server.service.WechatUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WechatUserServiceImpl implements WechatUserService {

    @Autowired
    private WechatUserRepository wechatUserRepository;

    @Autowired
    private AccessTokenService accessTokenService;


    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId
    @Value("${com.constant.weixin.secret}")
    public String secret;//自己在微信测试平台设置的secret


    @Override
    public void saveInDB(WechatUser wechatUser) {
        WechatUser user = wechatUserRepository.queryByOpenId(wechatUser.getOpenId());
        if (user != null) {
            log.debug(wechatUser.getNickName() + " ; " + wechatUser.getOpenId() + " ; 以前已经保存在数据库里啦。");
            return;
        }
        wechatUserRepository.save(wechatUser);
    }

    @Override
    public List<WechatUser> getAllFromDB() {
        return (List<WechatUser>) wechatUserRepository.findAll();
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
        String accessToken = accessTokenService.getToken();

        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        Map<String, Object> data = new HashMap<>();
        data.put("access_token", accessToken);
        data.put("openid", openId);
        data.put("lang", AiLangType.zh_CN.getCode());

        JSONObject resJson = HttpConnectionUtils.get(url, data);

        return WechatUser.fromJson(resJson.toString());
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
        String accessToken = accessTokenService.getToken();

        String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" + accessToken;

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

        List<WechatUser> wechatUserList = new ArrayList<>();
        JSONArray json = resJson.getJSONArray("user_info_list");

        if (json.size() > 0) {
            for (int i = 0; i < json.size(); i++) {
                JSONObject job = json.getJSONObject(i);
                WechatUser user = WechatUser.fromJson(resJson.toString());
                wechatUserList.add(user);
            }
        }

        return wechatUserList;
    }
}
