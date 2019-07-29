package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.enums.TicketType;
import net.joywise.wechat.server.service.TicketService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: JsapiTicketServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 16:14
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 16:14
 * @company: shopin.net
 * @version: V1.0
 */
@Slf4j
@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId

    @Value("${com.constant.weixin.secret}")
    public String appSecert;//自己在微信测试平台设置的secret


    private static final String TICKET_KEY = "wechat:ticket:";
    private static final int TIME_DIFFERENCE_LOSE = 60; //秒

    @Override
    public void save(String ticket, int expiresIn, TicketType ticketType) {
        String ticketKey = TICKET_KEY + ticketType.getCode();
        boolean hasKey = redisUtil.hasKey(ticketKey);
        if (hasKey) {
            log.warn("the key " + ticketKey + " already exists, overwrite it .");
        }
        redisUtil.set(ticketKey, ticket);
        redisUtil.expire(ticket, expiresIn - TIME_DIFFERENCE_LOSE);
    }

    @Override
    public String getTicket(String accessToken, TicketType ticketType) {
        String ticketKey = TICKET_KEY + ticketType.getCode();

        boolean hasKey = redisUtil.hasKey(ticketKey);
        if (hasKey) {
            return (String) redisUtil.get(ticketKey);
        }
        String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

        Map<String, Object> data = new HashMap<>();
        data.put("access_token", accessToken);
        data.put("type", ticketType.getCode());

        JSONObject response = HttpConnectionUtils.get(jsapi_ticket_url, data);
        log.debug("url:" + jsapi_ticket_url + "; params : " + data + " ;response: " + response);

        int errcode = response.getInteger("access_token");
        String errmsg = response.getString("errmsg");
        String ticket = response.getString("ticket");
        int expiresIn = response.getInteger("expires_in");

        //请求成功
        if (errcode == 0) {
            save(accessToken, expiresIn, ticketType);
            return accessToken;
        }
        log.error("get jsapiticket for accessToken" + accessToken + " failed. result is :" + response);

        return "";
    }

}
