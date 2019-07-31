package net.joywise.wechat.server.bean.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Oauth2AccessToken implements Serializable {

    private String accessToken;

    private String refreshToken;

    private String openId;

    private String scope;

    private int expiresIn = -1;

    public Oauth2AccessToken() {
    }

    public Oauth2AccessToken(String accessToken, String refreshToken, String openId, String scope, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.openId = openId;
        this.scope = scope;
        this.expiresIn = expiresIn;
    }

    public static Oauth2AccessToken fromJson(String json) {
        Oauth2AccessToken token = JSON.parseObject(json, new TypeReference<Oauth2AccessToken>() {
        });
        return token;
    }

    @Override
    public String toString() {
        return "Oauth2AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", openId='" + openId + '\'' +
                ", scope='" + scope + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", getAccessToken());
        map.put("expiresIn", getExpiresIn());
        map.put("openId", getOpenId());
        map.put("refreshToken", getRefreshToken());
        map.put("scope", getScope());

        return map;
    }
}
