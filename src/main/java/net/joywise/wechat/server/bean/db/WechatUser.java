package net.joywise.wechat.server.bean.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Setter
@Entity
@Table(name = "t_wechat_user", uniqueConstraints = {@UniqueConstraint(columnNames = "openId")})
public class WechatUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String nickName;
    private String openId;
    private boolean subscribe;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headImgUrl;
    private String subscribeTime;
    private String remark;
    private int groupId;
    private String[] tagIdList;
    private String subscribeScene;
    private String qrScene;
    private String qrSceneStr;
    private long schoolId;

    public WechatUser() {
    }


    public static WechatUser fromJson(String json) {
        WechatUser user = JSON.parseObject(json, new TypeReference<WechatUser>() {
        });
        return user;
    }

    @Override
    public String toString() {
        return "WechatUser{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", openId='" + openId + '\'' +
                ", subscribe=" + subscribe +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", subscribeTime=" + subscribeTime +
                ", remark='" + remark + '\'' +
                ", groupId=" + groupId +
                ", tagIdList=" + Arrays.toString(tagIdList) +
                ", subscribeScene='" + subscribeScene + '\'' +
                ", qrScene='" + qrScene + '\'' +
                ", qrSceneStr='" + qrSceneStr + '\'' +
                '}';
    }

}
