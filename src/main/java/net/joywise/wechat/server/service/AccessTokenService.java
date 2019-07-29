package net.joywise.wechat.server.service;

public interface AccessTokenService {

    void save(String accessToken, int expiresIn);

    String getToken();

}
