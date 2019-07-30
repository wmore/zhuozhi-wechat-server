package net.joywise.wechat.server.service;

import net.joywise.wechat.server.error.WxErrorException;

public interface BaseAccessTokenService {

    void save(String accessToken, int expiresIn);

    String getToken() throws WxErrorException;

}
