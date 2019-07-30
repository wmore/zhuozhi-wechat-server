package net.joywise.wechat.server.bean.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Id
    private Long id;

    private String openId;

    private Long userId;

    //所属学校
    private Long schoolId;

    // 手机号
    @Column(length = 11)
    private String telephoneNumber;

    //邮箱
    @Column(length = 50)
    private String email;

    //用户名
    @Column(length=20)
    private String userName;

    // 身份证号码
    @NotNull(message = "身份证号码不能为空")
    @Column(length = 18, nullable = false, unique = true)
    private String idNumber;

    // 姓名
    @NotNull(message = "姓名不能为空")
    @Column(length = 20, nullable = false)
    private String name;

    // 密码
    @JsonIgnore
    @Size(max=100,message="密码长度过长")
    @Column(length = 100, nullable = false)
    private String password;

    // 用户类型 0--学生 1--教师 2--平台用户 3--超级管理员
    @Column(nullable = false)
    private Integer userType;

    public SmartUser() {
    }
}
