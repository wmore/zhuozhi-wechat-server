package net.joywise.wechat.server.bean.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(name = "t_student", uniqueConstraints = {@UniqueConstraint(columnNames = {"studentId", "openId"})})
public class Student implements Serializable {

    private static final Long serialVersionUID = 7327782974049887711L;

    private String openId;

    @Id
    private Long studentId;

    //所属学校
    private Long schoolId;

    //所属班级
    private Long classId;

    private Long specialtyId;

    private Long acdemyId;

    //省份
    private Long provinceId;

    //城市
    private Long cityId;

    //区县
    private Long areaId;

    //手机号
    private String telephoneNumber;

    //邮箱
    private String email;

    //身份证号码
    private String idNumber;

    //姓名
    private String name;

    //性别   1--男  2--女
    private Integer sex;

    //学号
    private String studentNumber;

    //年级
    private String grade;

    //userId
    private Long userId;

    //用户名
    private String userName;
}
