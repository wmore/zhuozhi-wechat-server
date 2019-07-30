package net.joywise.wechat.server.service.impl;

import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.dao.SmartUserDao;
import net.joywise.wechat.server.service.SmartUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Title: SmartUserServiceImpl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/30 15:33
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/30 15:33
 * @company: shopin.net
 * @version: V1.0
 */
public class SmartUserServiceImpl implements SmartUserService {

    @Autowired
    private SmartUserDao smartUserDao;

    @Override
    public void saveInDB(SmartUser smartUser) {
        smartUserDao.save(smartUser);
    }

    @Override
    public List<SmartUser> getAllFromDB() {
        return (List<SmartUser>) smartUserDao.findAll();
    }

    @Override
    public SmartUser queryByOpenId(String openId) {
        return smartUserDao.queryByOpenId(openId);
    }

    @Override
    public SmartUser queryByUserIdAndSchoolId(Long userId, Long schoolId) {
        return smartUserDao.queryByUserIdAndSchoolId(userId, schoolId);
    }

    @Override
    public SmartUser queryByUserNameAndSchoolId(String userName, Long schoolId) {
        return smartUserDao.queryByUserNameAndSchoolId(userName, schoolId);
    }

    @Override
    public void deleteByOpenId(String openId) {
        SmartUser user = smartUserDao.queryByOpenId(openId);
        if (user != null) {
            smartUserDao.delete(user);
        }
    }
}
