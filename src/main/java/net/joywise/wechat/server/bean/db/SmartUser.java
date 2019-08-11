package net.joywise.wechat.server.bean.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @Title: SmartUser
 * @Description: smart平台用户的基本信息
 * @author: wyue
 * @date: 2019/7/30 14:55
 * @company: shopin.net
 * @version: V1.0
 */
@Data
@Entity
@Table(name = "t_smart_user", uniqueConstraints = {@UniqueConstraint(columnNames = "openId")})
public class SmartUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ApiModelProperty(value = "微信openid", required = true)
    @Column(nullable = false)
    private String openId;

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    //所属学校
    @ApiModelProperty(value = "所属学校", required = true)
    @NotNull(message = "机构id不能为空")
    private Long schoolId;

    // 手机号
    @Column(length = 11)
    private String telephoneNumber;

    //用户名
    @ApiModelProperty(value = "用户名", required = true)
    @Column(length = 20, nullable = false)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    // 用户类型 0--学生 1--教师 2--平台用户 3--超级管理员
    @ApiModelProperty(value = "用户类型 0--学生 1--教师 2--平台用户 3--超级管理员", required = true)
    @Column(nullable = false)
    private Integer userType;

    @ApiModelProperty(value = "微信的oauth2 的token")
    @Transient
    private String wxOauth2Token;

    @ApiModelProperty(value = "学校的智管理平台的token")
    @Transient
    private String smartToken;

    @ApiModelProperty(value = "学校的管理平台的url")
    @Transient
    private String smartUrl;

    @ApiModelProperty(value = "学校的socketio的url")
    @Transient
    private String sockitioUrl;

    public SmartUser() {
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
