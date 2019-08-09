package net.joywise.wechat.server.bean.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Oauth2AccessToken implements Serializable {

    @ApiModelProperty(value = "网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同", example = "")
    private String accessToken;
    @ApiModelProperty(value = "用户刷新access_token", example = "")
    private String refreshToken;
    @ApiModelProperty(value = "用户唯一标识", example = "")
    private String openId;
    @ApiModelProperty(value = "用户授权的作用域，使用逗号（,）分隔", example = "")
    private String scope;
    @ApiModelProperty(value = "access_token接口调用凭证超时时间，单位（秒）", example = "")
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
