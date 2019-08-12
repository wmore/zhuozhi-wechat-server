package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.constant.PLATFORM_URL;
import net.joywise.wechat.server.dao.SmartUserDao;
import net.joywise.wechat.server.enums.SmartUserErrorEnum;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

    private static final long SMART_TOKEN_EXPIRES_IN = 24 * 60 * 30;

    @Override
    public void saveInDB(SmartUser smartUser) {
        if (isSmartUserExist(smartUser.getOpenId())) {
            smartUserDao.save(smartUser);
        }
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
    public SmartUser bind(SmartUser smartUser) {
        SmartUser user = loginSmartPlatform(smartUser);
        if (user != null) {
            this.saveInDB(smartUser);
        }
        return user;
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
        if (schoolVo == null) {
            throw new CommonException(SmartUserErrorEnum.SCHOOL_INFO_NOT_FOUND);
        }
        String school_smart_address = schoolVo.getDomainAddressMapper();
        String school_socket_io_address = schoolVo.getSocketIOAddressMapper();

        school_smart_address = formatIpAddress(school_smart_address);
        school_socket_io_address = formatIpAddress(school_socket_io_address);

        String smartToken = "";
        boolean smartTokenIsValid = false;

        String smartTokenKey = CACHE_KEY.USER_SMART_TOKEN_KEY_PREFIX + smartUser.getOpenId();

        if (isSmartUserExist(smartUser.getOpenId())) {
            if (redisUtil.hasKey(smartTokenKey)) {
                smartToken = (String) redisUtil.get(smartTokenKey);
                JSONObject resultOfloginByToken = loginSmartPlatformByToken(school_smart_address, smartToken);

                if (resultOfloginByToken != null &&
                        resultOfloginByToken.getInteger("status") == 1) {
                    smartTokenIsValid = true;
                    smartToken = resultOfloginByToken.getString("token");
                }
            }
        }

        if (!smartTokenIsValid) {
            JSONObject resultOfloginByPwd = loginSmartPlatformByNameAndPwd(school_smart_address, smartUser.getUserName(), smartUser.getPassword());

            if (resultOfloginByPwd != null &&
                    resultOfloginByPwd.getInteger("status") == 1) {
                smartToken = resultOfloginByPwd.getString("token");
                //保存token到redis
                redisUtil.set(smartTokenKey, smartToken, SMART_TOKEN_EXPIRES_IN);
            } else {
                log.error("登录失败. logininfo :" + smartUser + "; result is :" + resultOfloginByPwd);
                throw new CommonException(SmartUserErrorEnum.PASSPORT_LOGIN_FAILED);
            }
        }

        smartUser.setSmartToken(smartToken);
        smartUser.setSmartUrl(school_smart_address);
        smartUser.setSockitioUrl(school_socket_io_address);

        return smartUser;
    }

    private String formatIpAddress(String ipAddress) {
        if (StringUtils.isNotEmpty(ipAddress) && ipAddress.endsWith("/")) {
            ipAddress = ipAddress.substring(0, ipAddress.length() - 1);
        }
        return ipAddress;
    }

    /***
     * 使用token登陆学校的passport
     * @param school_smart_address
     * @param smartToken
     * @return
     */
    private JSONObject loginSmartPlatformByToken(String school_smart_address, String smartToken) {
        String loginUrl = PLATFORM_URL.URL_HTTP_PREFIX + school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_PASSPORT_BY_TOKEN;
        HashMap<String, String> loginParms = new HashMap<>();
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

        try {
            String loginUrl = PLATFORM_URL.URL_HTTP_PREFIX + school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_PASSPORT;
            HashMap<String, String> loginParms = new HashMap<>();
            loginParms.put("username", userName);
            loginParms.put("password", URLEncoder.encode(password, "utf-8"));
            JSONObject resultJson = HttpConnectionUtils.post(loginUrl, loginParms);
            return resultJson;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     * 检查用户是否已经保存过数据库
     * @param openId
     * @return
     */
    private boolean isSmartUserExist(String openId) {
        return queryByOpenId(openId) != null;
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
