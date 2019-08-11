package net.joywise.wechat.server.bean.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_qrcode", indexes = {@Index(columnList = "sceneStr")})
public class QrCode implements Serializable {

    private static final long serialVersionUID = -5608758084249217405L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonIgnore
    private long id;

    /***
     * -1代表 永久
     */
    private int expireSeconds = -1;
    private String ticket;
    private String url;
    @Column(length = 128)
    private String sceneStr;

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

}
