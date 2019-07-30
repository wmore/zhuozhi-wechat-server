package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.db.SmartUser;

import java.util.List;

public interface SmartUserService {

    void saveInDB(SmartUser smartUser);

    List<SmartUser> getAllFromDB();

    SmartUser queryByOpenId(String openId);

    SmartUser queryByUserIdAndSchoolId(Long userId, Long schoolId);

    SmartUser queryByUserNameAndSchoolId(String userName, Long schoolId);

    void deleteByOpenId(String openId);

}
