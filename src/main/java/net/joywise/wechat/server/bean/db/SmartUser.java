package net.joywise.wechat.server.bean.db;

import io.swagger.annotations.ApiModelProperty;
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

    // 用户类型 0--学生 1--教师 2--平台用户 3--超级管理员
    @ApiModelProperty(value = "用户类型 0--学生 1--教师 2--平台用户 3--超级管理员", required = true)
    @Column(nullable = false)
    private Integer userType;

    public SmartUser() {
    }


}
