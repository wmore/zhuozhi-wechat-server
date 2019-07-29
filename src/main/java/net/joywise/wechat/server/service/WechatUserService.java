package net.joywise.wechat.server.service;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.dao.WechatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WechatUserService {

    void saveInDB(WechatUser wechatUser) ;

    List<WechatUser> getAllFromDB() ;

    /***
     * 获取单个微信用户信息
     * @param openId
     * @return
     */
    WechatUser getUserInfo(String openId);

    /***
     * 批量获取微信用户信息
     * @param openIdList
     * @return
     */
    List<WechatUser> batchGetUserInfo(List<String> openIdList);

}
