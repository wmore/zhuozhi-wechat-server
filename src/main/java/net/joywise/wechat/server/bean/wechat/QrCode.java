package net.joywise.wechat.server.bean.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.Serializable;

public class QrCode implements Serializable {

    private static final long serialVersionUID = -5608758084249217405L;

    /***
     * -1代表没有永久
     */
    private int expireSeconds = -1;
    private String ticket;
    private String url;

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public QrCode() {
    }

    public QrCode(int expireSeconds, String ticket, String url) {
        this.expireSeconds = expireSeconds;
        this.ticket = ticket;
        this.url = url;
    }

    public static QrCode fromJson(String json) {
        QrCode qrCode = JSON.parseObject(json, new TypeReference<QrCode>() {
        });
        return qrCode;
    }


    @Override
    public String toString() {
        return "QrCode{" +
                "expireSeconds=" + expireSeconds +
                ", ticket='" + ticket + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
