package net.joywise.wechat.server.service.impl;

import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.dao.SchoolDao;
import net.joywise.wechat.server.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Title: SchoolServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/9 16:29
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/9 16:29
 * @company: shopin.net
 * @version: V1.0
 */
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolDao schoolDao;

    @Override
    public List<SchoolVo> getSchoolList(boolean isWechatUsed) {
        return schoolDao.getSchoolList(isWechatUsed ? 1 : 0);
    }

    @Override
    public SchoolVo getSchoolById(long schoolId) {
        return schoolDao.getSchoolById(schoolId);
    }
}
