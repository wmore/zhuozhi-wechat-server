package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.vo.SchoolVo;

import java.util.List;

/**
 * @Title: SchoolService
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/9 16:28
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/9 16:28
 * @company: shopin.net
 * @version: V1.0
 */
public interface SchoolService {

    List<SchoolVo> getSchoolList(boolean isWechatUsed);

    SchoolVo getSchoolById(long schoolId);

}
