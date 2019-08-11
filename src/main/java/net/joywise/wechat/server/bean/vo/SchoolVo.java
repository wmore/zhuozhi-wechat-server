package net.joywise.wechat.server.bean.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 学校基础数据
 */
@Data
public class SchoolVo implements Serializable {

    private static final long serialVersionUID = -7109253945410914397L;

    private Long schoolId;

    private String schoolName;

    /***
     * 机构域名
     */
//    private String domain;

    /***
     * 机构类型  1--本科  2--高职  3--中职
     */
    private Integer schoolType;

    /***
     * 是否微信端使用的机构 true :是 false: 否
     */
    private Boolean isWechatUsed;

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

    public SchoolVo() {
    }
}
