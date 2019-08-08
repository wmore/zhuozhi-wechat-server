package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.wechat.message.News;
import net.joywise.wechat.server.bean.wechat.message.NewsMessage;
import net.joywise.wechat.server.bean.wechat.message.TextMessage;
import net.joywise.wechat.server.bean.wechat.WeixinMessageInfo;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.enums.AiLangType;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.service.MsgService;
import net.joywise.wechat.server.service.WechatUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.WeixinMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private WeixinMessageInfo weixinMessageInfo;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private BaseAccessTokenService baseAccessTokenService;

    @Value("${com.constant.weixin.templateId}")
    public String templateId;


    @Override
    public String handleWxMessage(HttpServletRequest request, HttpServletResponse response) {
        String respMessage = null;

        try {

            // 默认返回的文本消息内容
            String respContent = null;
            // xml分析
            // 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
            Map<String, String> map = WeixinMessageUtil.parseXml(request);
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

            // 分析用户发送的消息类型，并作出相应的处理

            // 文本消息
            if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
//                respContent = "亲，这是文本消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 图片消息
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
//                respContent = "您发送的是图片消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 语音消息
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
//                respContent = "您发送的是语音消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 视频消息
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
//                respContent = "您发送的是视频消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 地理位置消息
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
//                respContent = "您发送的是地理位置消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 链接消息
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_LINK)) {
//                respContent = "您发送的是链接消息！";
//                textMessage.setContent(respContent);
//                respMessage = WeixinMessageUtil.textMessageToXml(textMessage);
            }

            // 事件推送(当用户主动点击菜单，或者扫面二维码等事件)
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_EVENT)) {

                // 事件类型
                String eventType = map.get("Event");
                // 关注
                if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//                    respMessage = weixinMessageModelUtil.followResponseMessageModel(weixinMessageInfo);
                    wechatUserService.handleSubscribe(map);
                }
                // 取消关注
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
//                    weixinMessageModelUtil.cancelAttention(fromUserName);
                    wechatUserService.handleUnSubscribe(map);
                }
                // 扫描带参数二维码
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_SCAN)) {
                    System.out.println("扫描带参数二维码! sceneId : " + map.get("EventKey"));
//                    respMessage = wechatUserService.handleScanQrcode(map);
                    wechatUserService.handleScanQrcodeAndReplay(map);
                }
                // 上报地理位置
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_LOCATION)) {
                    System.out.println("上报地理位置");
                }
                // 自定义菜单（点击菜单拉取消息）
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_CLICK)) {

                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = map.get("EventKey");
                    System.out.println("eventKey------->" + eventKey);

                }
                // 自定义菜单（(自定义菜单URl视图)）
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_VIEW)) {
                    System.out.println("处理自定义菜单URI视图");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统出错！", e);
            respMessage = null;
        } finally {
            if (null == respMessage) {
                log.error("respMessage is null!!!!!!!!!!");
                // TODO:待处理
            }
        }

        return respMessage;
    }


    /***
     * 新建文本消息
     * @param fromUserName
     * @param toUserName
     * @param respContent
     * @return
     */
    @Override
    public String initTextMessage(String fromUserName, String toUserName, String respContent) {
        // 默认回复文本消息
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(WeixinMessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent(respContent);
        return WeixinMessageUtil.textMessageToXml(textMessage);
    }

    /***
     * 新建图文消息
     * @param toUserName
     * @param fromUserName
     * @param courseTeaching
     * @return
     */
    @Override
    public String initNewsMessage(String toUserName, String fromUserName, CourseTeaching courseTeaching) {
        List<News> newsList = new ArrayList<>();
        //图文消息实体
        NewsMessage newsMessage = new NewsMessage();
        //图文消息的内容实体
        News news = new News();
        news.setTitle(courseTeaching.getCourseName());
        news.setDescription(courseTeaching.getCourseName() + "二维条码/二维码（2-dimensional bar code）是用某种特定的几何图形按一定规律在平面（二维方向上）分布的黑白相间的图形记录数据符号信息的；在代码编制上巧妙地利用构成计算机内部逻辑基础的“0”、“1”比特流的概念，使用若干个与二进制相对应的几何形体来表示文字数值信息，通过图象输入设备或光电扫描设备自动识读以实现信息自动处理：它具有条码技术的一些共性：每种码制有其特定的字符集；每个字符占有一定的宽度；具有一定的校验功能等。同时还具有对不同行的信息自动识别功能、及处理图形旋转变化点。");
        news.setPicUrl(courseTeaching.getIndexImgUrl());//需要替换本地服务器图片文件
        news.setUrl(courseTeaching.getLessonUrl());
        newsList.add(news);

        newsMessage.setFromUserName(toUserName);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setMsgType(WeixinMessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        String message = WeixinMessageUtil.newsMessageToXml(newsMessage);
//        log.debug(message);
        return message;
    }

    @Override
    public boolean sendTemplateMessage(String toUserName, CourseTeaching courseTeaching) {
        String accessToken = null;
        try {
            accessToken = baseAccessTokenService.getToken();
            String url = WX_URL.URL_SEND_TEMPLATE_MESSAGE.replace("{accessToken}", accessToken);

            JSONObject postJson = new JSONObject();
            postJson.put("touser", toUserName);
            postJson.put("template_id", templateId);
            postJson.put("url", courseTeaching.getLessonUrl());

            JSONObject dataJson = new JSONObject();

            JSONObject firstJsonData = new JSONObject();
            firstJsonData.put("value", courseTeaching.getBeginTime() + " 有课程安排");
            firstJsonData.put("color", "#173177");
            dataJson.put("first", firstJsonData);

            JSONObject keyword1JsonData = new JSONObject();
            keyword1JsonData.put("value", courseTeaching.getCourseName());
            keyword1JsonData.put("color", "#173177");
            dataJson.put("keyword1", keyword1JsonData);

            JSONObject keyword2JsonData = new JSONObject();
            keyword2JsonData.put("value", courseTeaching.getTeacherName());
            keyword2JsonData.put("color", "#173177");
            dataJson.put("keyword2", keyword2JsonData);

            JSONObject keyword3JsonData = new JSONObject();
            keyword3JsonData.put("value", courseTeaching.getClassName());
            keyword3JsonData.put("color", "#173177");
            dataJson.put("keyword3", keyword3JsonData);

            JSONObject remarkJsonData = new JSONObject();
            remarkJsonData.put("value", "点击进入课程学习。");
            remarkJsonData.put("color", "#173177");
            dataJson.put("remark", remarkJsonData);

            postJson.put("data", dataJson);

            JSONObject resJson = HttpConnectionUtils.post(url, postJson);
            if (resJson.getInteger("errcode") == 0 &&
                    resJson.getString("errmsg").equals("ok")) {
                return true;
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }
        return false;
    }
}
