package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.error.WxError;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Title: BaseAccessTokenServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 13:49
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 13:49
 * @company: shopin.net
 * @version: V1.0
 */
@Slf4j
@Service
public class BaseAccessTokenServiceImpl implements BaseAccessTokenService {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId

    @Value("${com.constant.weixin.secret}")
    public String appSecert;//自己在微信测试平台设置的secret


    private static final String ACCESS_TOKEN_KEY = "wechat:accessToken";
    private static final int TIME_DIFFERENCE_LOSE = 60; //秒

    protected Lock accessTokenLock = new ReentrantLock();

    public void save(String accessToken, int expiresIn) {
        boolean hasKey = redisUtil.hasKey(ACCESS_TOKEN_KEY);
        if (hasKey) {
            log.warn("the key " + ACCESS_TOKEN_KEY + " already exists, overwrite it .");
        }
        redisUtil.set(ACCESS_TOKEN_KEY, accessToken, expiresIn - TIME_DIFFERENCE_LOSE);
    }

    public String getToken() throws WxErrorException {
        boolean hasKey = redisUtil.hasKey(ACCESS_TOKEN_KEY);
        if (hasKey) {
            return (String) redisUtil.get(ACCESS_TOKEN_KEY);
        }

        accessTokenLock.lock();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("grant_type", "client_credential");
            data.put("appid", appId);
            data.put("secret", appSecert);

            JSONObject response = HttpConnectionUtils.get(WX_URL.URL_GET_TOKEN, data);
            log.debug("url:" + WX_URL.URL_GET_TOKEN + "; params : " + data + " ;response: " + response);

            WxError error = WxError.fromJson(response);
            if (error.getErrCode() != 0) {
                throw new WxErrorException(error);
            }

            String accessToken = response.getString("access_token");
            int expiresIn = response.getInteger("expires_in");

            save(accessToken, expiresIn);
            return accessToken;

        } finally {
            accessTokenLock.unlock();
        }

    }

}
