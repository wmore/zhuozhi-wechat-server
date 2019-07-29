package net.joywise.wechat.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: MsgService
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:34
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:34
 * @company: shopin.net
 * @version: V1.0
 */
public interface MsgService {
    /***
     * 处理微信信息
     * @param request
     * @param response
     * @return
     */
    String handleWxMessage(HttpServletRequest request, HttpServletResponse response);
}
