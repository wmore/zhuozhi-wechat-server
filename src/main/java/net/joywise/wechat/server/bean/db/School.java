package net.joywise.wechat.server.bean.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 学校基础数据
 *
 * @author longweier
 * @Description:
 * @Copyright: 福建卓智网络科技有限公司 (c)2016
 * @Created Date : 2016年3月15日
 * @vesion 1.0
 */
@Data
@Entity
@Table(name = "`base-platform`.t_school")
public class School implements Serializable {

    private static final long serialVersionUID = 7607655739237259980L;

    //ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    //机构名称
    @NotNull(message = "机构名称不能为空")
    @Size(max = 40, message = "机构名称长度不能超过40个字符")
    @Column(length = 100, nullable = false)
    private String schoolName;

    //机构域名
    @NotNull(message = "机构域名不能为空")
    @Pattern(regexp = "[a-zA-Z0-9-]+", message = "机构域名仅支持数字、小写英文字母及下划线输入")
    @Size(max = 10, message = "机构名称长度不能超过10个字符")
    @Column(length = 50, nullable = false, unique = true)
    private String domain;

    //机构类型  1--本科  2--高职  3--中职
    @NotNull(message = "机构类型不能为空")
    @Column(nullable = false)
    private Integer schoolType;


    //是否上线
    @Column(nullable = false)
    private Boolean isOnline;

    //联系人
    @Size(max = 50, message = "联系人长度不能超过50个字符")
    @Column(length = 50)
    private String contacts;

    //联系电话
    @Size(max = 50, message = "联系电话长度不能超过50个字符")
    @Column(length = 50)
    private String telephoneNumber;

    //学校logo（校徽）
    @Column(length = 100)
    @Size(max = 100, message = "Logo（校徽）图片路径长度不能超过50个字符")
    private String logoPath;

    //学校logo（校名+校徽）
    @Column(length = 100)
    @Size(max = 100, message = "Logo（校徽+校名）图片路径长度不能超过50个字符")
    private String logoPath2;


    //创建时间
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;


    //最后修改时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    //是否删除
    @Column(nullable = false)
    private Boolean isRemove;

    @JsonIgnore
    @OneToOne(mappedBy = "school")
    private SchoolEdition schoolEdition;

    //版本类型 1-互动课堂版 2-慕课版
    @Transient
    private Integer editionType;

    //部署方式 1-内网部署 2-外网部署
    @Transient
    private Integer deployType;

    //是否有授课助手PAD端
    @Transient
    private Boolean appAssistant;

    //是否有课堂录播
    @Transient
    private Boolean broadcast;

    //雨盒数量
    @Transient
    private Long cloudBoxCount;

    //序列号
    @Column(length = 50)
    private String serialNumber;

    //激活状态 true :已激活 false: 未激活
    @Column
    private Boolean activation;


    @Column
    private String bigdataSerialNumber;


    //是否微信端使用的机构 true :是 false: 否
    @Column
    private Boolean isWechatUsed;


}
