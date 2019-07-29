package net.joywise.wechat.server.controller;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@EnableAutoConfiguration
public class WechatRestController {

    @Autowired
    private AuthService authService;


    /**
     * 微信接入校验接口
     *
     * @param request
     * @throws UnsupportedEncodingException
     */
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



}
