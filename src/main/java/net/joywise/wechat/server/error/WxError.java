package net.joywise.wechat.server.error;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class WxError implements Serializable {
    private static final long serialVersionUID = 7869786563361406291L;

    /**
     * 微信错误代码.
     */
    private int errCode;

    /**
     * 微信错误信息.
     * （如果可以翻译为中文，就为中文）
     */
    private String errMsg;

    public WxError() {
    }

    public static WxError fromJson(JSONObject json) {
        WxError wxError = JSON.toJavaObject(json, WxError.class);
        return wxError;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("errCode", this.errCode);
        map.put("errMsg", this.errMsg);

        return map;
    }

}
