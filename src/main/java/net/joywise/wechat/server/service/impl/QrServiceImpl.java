package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.wechat.QrCode;
import net.joywise.wechat.server.constant.WX_URL;
import net.joywise.wechat.server.error.WxErrorException;
import net.joywise.wechat.server.service.BaseAccessTokenService;
import net.joywise.wechat.server.service.QrService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Title: QrServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/31 13:53
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/31 13:53
 * @company: shopin.net
 * @version: V1.0
 */
@Slf4j
@Service
public class QrServiceImpl implements QrService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BaseAccessTokenService baseAccessTokenService;

    private static final String QR_SCENE_KEY = "wechat:qr:scene:";
    private static final String QR_LIMIT_SCENE_KEY = "wechat:qr:limit:scene:";

    //生成永久二维码ticket scene_str json
    private static final String QRCODE_PARAMS_LIMIT_WITH_SCENE_STR = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"SCENESTR\"}}}";

    //生成临时二维码ticket scene_str json
    private static final String QRCODE_PARAMS_WITH_SCENE_STR = "{\"expire_seconds\": SECONDS, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"SCENESTR\"}}}";


    @Override
    public QrCode getTempQrTicketWithScene(int expireSeconds, String scene) {
        String redisKey = QR_SCENE_KEY + scene;

        boolean hasKey = redisUtil.hasKey(redisKey);
        if (hasKey) {
            String qrcodeVal = (String) redisUtil.get(redisKey);
            QrCode qrCode = QrCode.fromJson(qrcodeVal);
            qrCode.setExpireSeconds((int) redisUtil.getExpire(redisKey));
            return qrCode;
        }

        try {
            String accessToken = baseAccessTokenService.getToken();
            String url = WX_URL.URL_GET_QRCODE.replace("{accessToken}", accessToken);

            String reqStr = QRCODE_PARAMS_WITH_SCENE_STR.replace("SECONDS", String.valueOf(expireSeconds))
                    .replace("SCENESTR", String.valueOf(scene));

            JSONObject postJson = JSON.parseObject(reqStr);

            JSONObject resultJson = HttpConnectionUtils.post(url, postJson);

            redisUtil.set(redisKey, resultJson.toString(), resultJson.getLongValue("expire_seconds"));

            return QrCode.fromJson(resultJson.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }

        return null;
    }

    @Override
    public QrCode getQrTicketWithScene(String scene) {
        String redisKey = QR_LIMIT_SCENE_KEY + scene;

        boolean hasKey = redisUtil.hasKey(redisKey);
        if (hasKey) {
            String qrcodeVal = (String) redisUtil.get(redisKey);
            QrCode qrCode = QrCode.fromJson(qrcodeVal);
            return qrCode;
        }

        try {
            String accessToken = baseAccessTokenService.getToken();
            String url = WX_URL.URL_GET_QRCODE.replace("{accessToken}", accessToken);

            String reqStr = QRCODE_PARAMS_LIMIT_WITH_SCENE_STR.replace("SCENESTR", String.valueOf(scene));

            JSONObject postJson = JSON.parseObject(reqStr);

            JSONObject resultJson = HttpConnectionUtils.post(url, postJson);

            redisUtil.set(redisKey, resultJson.toString());

            return QrCode.fromJson(resultJson.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("Try get token has error!", e);
        }

        return null;
    }
}