package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.enums.TicketType;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.service.JSAPITicketService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
public class JSAPITicketServiceImpl implements JSAPITicketService {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId

    @Value("${com.constant.weixin.secret}")
    public String appSecert;//自己在微信测试平台设置的secret

    @Autowired
    private BaseAccessTokenService baseAccessTokenService;


    private static final int TIME_DIFFERENCE_LOSE = 60; //秒

    @Override
    public void save(String ticket, int expiresIn, TicketType ticketType) {
        String ticketKey = CACHE_KEY.TICKET_KEY_PREFIX + ticketType.getCode();
        boolean hasKey = redisUtil.hasKey(ticketKey);
        if (hasKey) {
            log.warn("the key " + ticketKey + " already exists, overwrite it .");
        }
        redisUtil.set(ticketKey, ticket, expiresIn - TIME_DIFFERENCE_LOSE);
    }

    @Override
    public String getTicket(String accessToken, TicketType ticketType) {
        String ticketKey = CACHE_KEY.TICKET_KEY_PREFIX + ticketType.getCode();

        boolean hasKey = redisUtil.hasKey(ticketKey);
        if (hasKey) {
            return (String) redisUtil.get(ticketKey);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("access_token", accessToken);
        data.put("type", ticketType.getCode());

        JSONObject response = HttpConnectionUtils.get(WX_URL.URL_GET_JSAPI_TICKET, data);
        log.debug("url:" + WX_URL.URL_GET_JSAPI_TICKET + "; params : " + data + " ;response: " + response);

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


    public String accessToken = null;

    @Override
    public Map<String, String> getSign(String url) {
        try {
            accessToken = baseAccessTokenService.getToken();
            String jsapi_ticket = getTicket(accessToken, TicketType.JSAPI);

            Map<String, String> ret = new HashMap<String, String>();
            String nonce_str = create_nonce_str();
            String timestamp = create_timestamp();
            String string1;
            String signature = "";

            //注意这里参数名必须全部小写，且必须有序
            string1 = "jsapi_ticket=" + jsapi_ticket +
                    "&noncestr=" + nonce_str +
                    "&timestamp=" + timestamp +
                    "&url=" + url;
            System.out.println("string1=" + string1);

            try {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(string1.getBytes("UTF-8"));
                signature = byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ret.put("url", url);
            ret.put("jsapi_ticket", jsapi_ticket);
            ret.put("nonceStr", nonce_str);
            ret.put("timestamp", timestamp);
            ret.put("signature", signature);
            ret.put("appId", appId);

            log.info("1.ticket(原始)=" + jsapi_ticket);
            log.info("2.url=" + ret.get("url"));
            log.info("3.jsapi_ticket（处理后）=" + ret.get("jsapi_ticket"));
            log.info("4.nonceStr=" + ret.get("nonceStr"));
            log.info("5.signature=" + ret.get("signature"));
            log.info("6.timestamp=" + ret.get("timestamp"));

            return ret;
        } catch (
                WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }
        return null;
    }


    /**
     * 随机加密
     *
     * @param hash
     * @return
     */
    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 产生随机串--由程序自己随机产生
     *
     * @return
     */
    private String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 由程序自己获取当前时间
     *
     * @return
     */
    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
