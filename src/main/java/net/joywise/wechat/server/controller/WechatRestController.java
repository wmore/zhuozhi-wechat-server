package net.joywise.wechat.server.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.service.AuthService;
import net.joywise.wechat.server.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@EnableAutoConfiguration
public class WechatRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MsgService msgService;


    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId
    @Value("${com.constant.weixin.secret}")
    public String secret;//自己在微信测试平台设置的secret


    /**
     * 微信接入校验接口
     *
     * @param request
     * @throws UnsupportedEncodingException
     */
    @ApiIgnore
    @ApiOperation("给公众号使用的，微信接入校验接口")
    @RequestMapping(value = "/wx", method = RequestMethod.GET)
    public String handlePublicMsg(HttpServletRequest request) throws UnsupportedEncodingException {
        //设置编码，不然接收到的消息乱码
        request.setCharacterEncoding("UTF-8");
        //微信加密签名
        String signature = request.getParameter("signature");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        //随机数
        String nonce = request.getParameter("nonce");
        //随机字符串
        String echostr = request.getParameter("echostr");

        //接入验证
        if (authService.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }

        log.error("不是微信服务器发过来的请求，请小心！");
        return null;
    }

    @ApiIgnore
    @ApiOperation(value = "给公众号使用的，处理公众号的消息")
    @RequestMapping(value = "/wx", method = RequestMethod.POST)
    public String handlePublicMsg(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String respXml = msgService.handleWxMessage(request, response);
        if (StringUtils.isEmpty(respXml)) {
            System.out.println("-------------处理微信消息失败-----------------------");
            return null;
        } else {
            System.out.println("----------返回微信消息处理结果-----------------------:" + respXml);
            return respXml;
        }

    }

    @ApiOperation("获取公众号平台的参数 appId和appSecret")
    @RequestMapping(value = "/app_info", method = RequestMethod.GET)
    public String getWxInfo() {
        JSONObject result = new JSONObject();
        result.put("appId", appId);
        result.put("appSecret", secret);
        return result.toString();
    }

}
