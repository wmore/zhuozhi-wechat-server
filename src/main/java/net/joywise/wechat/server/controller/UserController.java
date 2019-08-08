package net.joywise.wechat.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.wechat.Oauth2AccessToken;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.SmartUserService;
import net.joywise.wechat.server.service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private SmartUserService smartUserService;

    @ApiOperation("保存智课堂平台的用户信息")
    @RequestMapping(value = "/{openId}", method = RequestMethod.GET)
    public ServiceResult getUser(@PathParam(value = "openId") String openId) {
        ServiceResult result = new ServiceResult(true);
        SmartUser userExist = smartUserService.queryByOpenId(openId);
        if (userExist != null) {
            result.setSuccess(true);
            result.setData(userExist);
        } else {
            result.setSuccess(false);
            result.setMessage("user is null, need save!");
        }
        return result;
    }

    @ApiOperation("保存智课堂平台的用户信息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ServiceResult save(@RequestBody SmartUser smartUser) {
        ServiceResult result = new ServiceResult(true);
        SmartUser userExist = smartUserService.queryByOpenId(smartUser.getOpenId());
        if (userExist != null) {
            result.setSuccess(false);
            result.setMessage("该用户已经存在，无需再次绑定！");
        } else {
            smartUserService.saveInDB(smartUser);
        }
        return result;
    }

    /***
     * 返回
     * {
     * 	"accessToken": "24_ttPntKPq-zlHkgiDzKk3VaWCyp67C89JaoiULKRG2t2PG-Rqlatx7m_6hD4xwEp0pha8zGzlzMsf9xHqtI9-JQ",
     * 	"expiresIn": 7200,
     * 	"openId": "o8zxfwjHfRahuXx0A2zGXoeaX2Og",
     * 	"refreshToken": "24_FoDqMamzkBgRqeqcXxvUtSJJ-1s5f-Uz34W2b055rXAiBEzixezjGRGHuy0mN0MF_iDEyymweS6Qs_wVbU0GxA",
     * 	"scope": "snsapi_base"
     * }
     * @param code
     * @param state
     * @return
     */
    @ApiOperation("获取用户oauth的token")
    @ApiImplicitParam(name = "code",
            value = "code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。须通过授权页面同意授权，获取code",
            required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/oauth2", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResult<Oauth2AccessToken> doOauth2(@RequestParam String code, @ApiIgnore String state) {
        ServiceResult result = new ServiceResult(false);

        try {
            Oauth2AccessToken token = wechatUserService.getOauth2AccessTokenByCode(code);
            log.debug("Oauth2AccessToken is : " + token.toString());
            result.setSuccess(true);

            result.setData(token);
            return result;
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("doOauth2", e);
            result.setProperty(e.getError().toMap());
        }

        return result;
    }

}
