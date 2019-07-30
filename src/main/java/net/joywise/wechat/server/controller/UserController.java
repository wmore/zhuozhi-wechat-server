package net.joywise.wechat.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.wechat.Oauth2AccessToken;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: UserController
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:21
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:21
 * @company: shopin.net
 * @version: V1.0
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WechatUserService wechatUserService;


    @RequestMapping(value = "/binding", method = RequestMethod.GET)
    public String bingding() {

        return "bingding";
    }

    @RequestMapping(value = "/appid", method = RequestMethod.GET)
    public String getAppId(String code, String state) {
        try {
            Oauth2AccessToken token = wechatUserService.getOauth2AccessTokenByCode(code);
            log.debug("Oauth2AccessToken is : " + token.toString());
            return JSON.toJSONString(token);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "";
    }

}
