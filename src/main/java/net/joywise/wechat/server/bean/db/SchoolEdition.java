package net.joywise.wechat.server.bean.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author longweier
 * @Description: 学校版本信息model
 * @Copyright: 福建卓智网络科技有限公司 (c)2016
 * @Created Date : 2017年3月3日
 * @vesion 1.0
 */
@Data
@Entity
@Table(name = "`base-platform`.t_school_edition")
public class SchoolEdition implements Serializable {

    private static final long serialVersionUID = 6584016817786246025L;

    //版本ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long editionId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolId")
    private School school;

    //所属学校
    @Transient
    @JsonIgnore
    private Long schoolId;

    //版本类型 1-V1.0 2-V2.0 3-虚拟 （弃用）
    @Column(nullable = false)
    private Integer editionType;

    //是否有课程分析权限（弃用）
    @Column
    private Boolean teachingAnalysis;

    //是否有课程中心权限
    @Column
    private Boolean courseCenter;

    //部署方式 1-内网部署 2-外网部署
    @Column(nullable = false)
    private Integer deployType;

    //创建时间
    @Column(nullable = false)
    private String createTime;

    //参加人ID
    @Column(nullable = false)
    private Long createrId;

    //学校顶级域名
    @Column(length = 100)
    private String topDomainAddress;

    //服务器域名访问地址
    @Column(length = 100)
    private String domainAddress;

    //socketIO地址
    @Column(length = 100)
    private String socketIOAddress;

    //是否有智课堂平台权限（包括【教学管理平台】【教师工作站】【课程中心】【学习中心】及【智课堂移动端】）
    @Column(nullable = false)
    private Boolean smartClass;

    //互动教学系统 (学生智课堂、授课助手APP、学生PC客户端启动后，并给予反馈提示：“软件未授权，不可使用！)
    @Column(nullable = false)
    private Boolean interactionManage;

    //智管理平台权限（web端）
    @Column(nullable = false)
    private Boolean smartManage;

    //智管理平台权限（移动端）
    @Column(nullable = false)
    private Boolean smartApiManage;

    //智评估平台权限 对应的Main
    @Column(nullable = false)
    private Boolean smartMainManage;

    //激活码
    @Transient
    @JsonIgnore
    private String serialNumber;

    //软件到期时间
    @Column(length = 50)
    private String expireDate;

    //软件到期时间
    @Column(length = 50)
    private String bigdataExpireTime;

    //软件到期时间
    @Column
    private Boolean bigdataCapture;

    //软件到期时间
    @Column
    private Boolean bigdataAnalysis;

    //软件到期时间
    @Column
    private Boolean bigdataSmartCenter;

    /**
     * 服务器域名 外网映射 访问地址
     */
    @Column(length = 100)
    private String domainAddressMapper;

    /**
     * socketIO地址 外网映射 访问地址
     */
    @Column(length = 100)
    private String socketIOAddressMapper;

}
