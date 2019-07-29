package net.joywise.wechat.server.bean.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

@Entity
//指定表名，指定唯一约束
@Table(name = "t_teacher", uniqueConstraints = {@UniqueConstraint(columnNames = {"teacherId", "openId"})})
public class Teacher implements Serializable {

    private static final long serialVersionUID = 4648915668810344080L;

    private String openId;

    //ID
    @Id
    private Long teacherId;

    //所属学校
    private Long schoolId;

    //所属班级
    private Long classId;

    private Long specialtyId;

    private Long acdemyId;


    //所属部门
    private Long departmentId;

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

    //出生日期
    private Date birthDay;

    //工号
    private String code;

    //职称  1--助教  2--讲师  3--副教授 4--教授  5--其他
    private Integer post;

    private Long userId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Long getAcdemyId() {
        return acdemyId;
    }

    public void setAcdemyId(Long acdemyId) {
        this.acdemyId = acdemyId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
