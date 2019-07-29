package net.joywise.wechat.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.wechat.TextMessage;
import net.joywise.wechat.server.bean.wechat.WeixinMessageInfo;
import net.joywise.wechat.server.service.MsgService;
import net.joywise.wechat.server.service.WechatUserService;
import net.joywise.wechat.server.util.WeixinMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Title: MsgServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:35
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:35
 * @company: shopin.net
 * @version: V1.0
 */
@Slf4j
@Service
public class MsgServiceImpl implements MsgService {
    @Autowired
    private WeixinMessageUtil weixinMessageUtil;
    @Autowired
    private WeixinMessageInfo weixinMessageInfo;
    @Autowired
    private WechatUserService wechatUserService;


    @Override
    public String handleWxMessage(HttpServletRequest request, HttpServletResponse response) {
        String respMessage = null;

        try {

            // 默认返回的文本消息内容
            String respContent = null;
            // xml分析
            // 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
            Map<String, String> map = weixinMessageUtil.parseXml(request);
            // 发送方账号
            String fromUserName = map.get("FromUserName");
            weixinMessageInfo.setFromUserName(fromUserName);
            System.out.println("fromUserName--->" + fromUserName);
            // 接受方账号（公众号）
            String toUserName = map.get("ToUserName");
            weixinMessageInfo.setToUserName(toUserName);
            System.out.println("toUserName----->" + toUserName);
            // 消息类型
            String msgType = map.get("MsgType");
            weixinMessageInfo.setMessageType(msgType);
            System.out.println("fromUserName is:" + fromUserName + " toUserName is:" + toUserName + " msgType is:" + msgType);

            // 默认回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(weixinMessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            // 分析用户发送的消息类型，并作出相应的处理

            // 文本消息
            if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
//                respContent = "亲，这是文本消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 图片消息
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
//                respContent = "您发送的是图片消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 语音消息
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
//                respContent = "您发送的是语音消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 视频消息
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
//                respContent = "您发送的是视频消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 地理位置消息
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
//                respContent = "您发送的是地理位置消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 链接消息
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_LINK)) {
//                respContent = "您发送的是链接消息！";
//                textMessage.setContent(respContent);
//                respMessage = weixinMessageUtil.textMessageToXml(textMessage);
            }

            // 事件推送(当用户主动点击菜单，或者扫面二维码等事件)
            else if (msgType.equals(weixinMessageUtil.REQ_MESSAGE_TYPE_EVENT)) {

                // 事件类型
                String eventType = map.get("Event");
                System.out.println("eventType------>" + eventType);
                // 关注
                if (eventType.equals(weixinMessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//                    respMessage = weixinMessageModelUtil.followResponseMessageModel(weixinMessageInfo);
                    String eventKey = map.get("EventKey");
                    if (eventKey.startsWith("qrscene")) {
                        respContent = "成功关注，并且您已扫描带参数二维码！ sceneId : " + eventKey.substring(8, eventKey.length());
                        textMessage.setContent(respContent);
                        respMessage = weixinMessageUtil.textMessageToXml(textMessage);

                        String openId = map.get("FromUserName");
                        WechatUser user = wechatUserService.getUserInfo(openId);
                        wechatUserService.saveInDB(user);

                    }
                }
                // 取消关注
                else if (eventType.equals(weixinMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
//                    weixinMessageModelUtil.cancelAttention(fromUserName);
                }
                // 扫描带参数二维码
                else if (eventType.equals(weixinMessageUtil.EVENT_TYPE_SCAN)) {
                    System.out.println("扫描带参数二维码! sceneId : " + map.get("EventKey"));
                    respContent = "您已扫描带参数二维码！ sceneId : " + map.get("EventKey");
                    textMessage.setContent(respContent);
                    respMessage = weixinMessageUtil.textMessageToXml(textMessage);
                }
                // 上报地理位置
                else if (eventType.equals(weixinMessageUtil.EVENT_TYPE_LOCATION)) {
                    System.out.println("上报地理位置");
                }
                // 自定义菜单（点击菜单拉取消息）
                else if (eventType.equals(weixinMessageUtil.EVENT_TYPE_CLICK)) {

                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = map.get("EventKey");
                    System.out.println("eventKey------->" + eventKey);

                }
                // 自定义菜单（(自定义菜单URl视图)）
                else if (eventType.equals(weixinMessageUtil.EVENT_TYPE_VIEW)) {
                    System.out.println("处理自定义菜单URI视图");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("系统出错");
            respMessage = null;
        } finally {
            if (null == respMessage) {
//                respMessage = weixinMessageModelUtil.systemErrorResponseMessageModel(weixinMessageInfo);
                log.error("respMessage is null!!!!!!!!!!");
                // TODO:待处理
            }
        }

        return respMessage;
    }
}
