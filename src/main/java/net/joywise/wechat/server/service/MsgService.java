package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.db.CourseTeaching;

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

    /***
     * 新建文字消息
     * @param fromUserName
     * @param toUserName
     * @param respContent
     * @return
     */
    String initTextMessage(String fromUserName, String toUserName, String respContent);

    /***
     * 新建图文消息
     * @param toUserName
     * @param fromUserName
     * @param courseTeaching
     * @return
     */
    String initNewsMessage(String toUserName, String fromUserName, CourseTeaching courseTeaching);

    /***
     * 新建模板消息
     * @param toUserName
     * @param courseTeaching
     * @return
     */
    boolean sendTemplateMessage(String toUserName, CourseTeaching courseTeaching);
}
