package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.constant.PLATFORM_URL;
import net.joywise.wechat.server.dao.SmartUserDao;
import net.joywise.wechat.server.error.CommonException;
import net.joywise.wechat.server.service.SchoolService;
import net.joywise.wechat.server.service.SmartUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Slf4j
@Service
public class SmartUserServiceImpl implements SmartUserService {

    @Autowired
    private SmartUserDao smartUserDao;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RedisUtil redisUtil;

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

    @Override
    public void bind(SmartUser smartUser) {
        SmartUser user = loginSmartPlatform(smartUser);
        if (user != null) {
            this.saveInDB(smartUser);
        }
    }

    /***
     * 登陆学习的smart 平台
     * @param smartUser
     * @return
     */
    @Override
    public SmartUser loginSmartPlatform(SmartUser smartUser) {
        long schoolId = smartUser.getSchoolId();
        SchoolVo schoolVo = schoolService.getSchoolById(schoolId);
        Assert.notNull(schoolVo, "not found the school!");
        String school_smart_address = schoolVo.getDomainAddressMapper();
        String school_socket_io_address = schoolVo.getSocketIOAddressMapper();

        if (StringUtils.isNotEmpty(school_smart_address) && school_smart_address.endsWith("/")) {
            school_smart_address = school_smart_address.substring(0, school_smart_address.length() - 1);
        }

        if (StringUtils.isNotEmpty(school_socket_io_address) && school_socket_io_address.endsWith("/")) {
            school_socket_io_address = school_socket_io_address.substring(0, school_socket_io_address.length() - 1);
        }

        String smartToken = "";
        String smartTokenKey = CACHE_KEY.USER_SMART_TOKEN_KEY_PREFIX + smartUser.getOpenId();
        if (redisUtil.hasKey(smartTokenKey)) {
            smartToken = (String) redisUtil.get(smartTokenKey);
            JSONObject resultOfloginByToken = loginSmartPlatformByToken(school_smart_address, smartToken);

            if (resultOfloginByToken.getInteger("status") == 1) {
                smartToken = resultOfloginByToken.getString("token");
            }
            {
                JSONObject resultOfloginByPwd = loginSmartPlatformByNameAndPwd(school_smart_address, smartUser.getUserName(), smartUser.getPassword());
                int status = resultOfloginByPwd.getInteger("status");
                if (status == 1) {
                    smartToken = resultOfloginByToken.getString("token");
                } else {
                    throw new CommonException("登陆失败！smart平台返回状态码：" + status);
                }
            }
        }

        smartUser.setSmartToken(smartToken);
        smartUser.setSmartUrl(school_smart_address);
        smartUser.setSockitioUrl(school_socket_io_address);
        return smartUser;
    }

    private JSONObject loginSmartPlatformByToken(String school_smart_address, String smartToken) {
        String loginUrl = school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_PASSPORT_BY_TOKEN;
        MultiValueMap loginParms = new LinkedMultiValueMap();
        loginParms.put("token", smartToken);

        JSONObject resultJson = HttpConnectionUtils.post(loginUrl, loginParms);
        return resultJson;
    }

    /***
     * 使用用户名、密码登陆学校的passport
     * @param school_smart_address
     * @param userName
     * @param password
     * @return
     */
    private JSONObject loginSmartPlatformByNameAndPwd(String school_smart_address, String userName, String password) {
        String loginUrl = school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_PASSPORT;
        Map<String, Object> loginParms = new HashMap<>();
        loginParms.put("username", userName);
        loginParms.put("password", password);

        JSONObject resultJson = HttpConnectionUtils.get(loginUrl, loginParms);
        return resultJson;
    }

    @Override
    public SmartUser getUserInfo(String openId) {
        SmartUser user = this.queryByOpenId(openId);
        if (user != null) {
            return loginSmartPlatform(user);
        }
        return null;
    }
}
