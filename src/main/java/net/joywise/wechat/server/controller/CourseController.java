package net.joywise.wechat.server.controller;

import io.swagger.annotations.Api;
import net.joywise.wechat.server.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Title: MsgController
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:33
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:33
 * @company: shopin.net
 * @version: V1.0
 */
@Api("一句话描述文档说明")
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private MsgService msgService;


    @RequestMapping(value = "/wx", method = RequestMethod.POST)
    public String handlePublicMsg2(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

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
}
