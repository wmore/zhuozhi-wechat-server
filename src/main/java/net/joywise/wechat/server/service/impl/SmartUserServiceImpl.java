package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.constant.PLATFORM_URL;
import net.joywise.wechat.server.dao.SmartUserDao;
import net.joywise.wechat.server.enums.SmartUserErrorEnum;
import net.joywise.wechat.server.enums.UserTypeEnum;
import net.joywise.wechat.server.error.CommonException;
import net.joywise.wechat.server.service.SchoolService;
import net.joywise.wechat.server.service.SmartUserService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import net.joywise.wechat.server.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
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
@Slf4j
@Service
public class SmartUserServiceImpl implements SmartUserService {

    @Autowired
    private SmartUserDao smartUserDao;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RedisUtil redisUtil;

    private static final long USER_INFO_EXPIRES_IN = 24 * 60 * 30 - 60;

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
        if (user == null) {
            throw new CommonException(SmartUserErrorEnum.USER_NOT_FOUND);
        }
        smartUserDao.delete(user);

    }

    @Override
    public SmartUser bind(SmartUser smartUser) {
        SmartUser user = loginSmartPlatform(smartUser);
        return user;
    }

    @Override
    public void unBind(String openId) {
        this.deleteByOpenId(openId);
        String key = CACHE_KEY.SMART_USER_INFO_KEY_PREFIX + openId;
        if (redisUtil.hasKey(key)) {
            JSONObject info = JSONObject.parseObject((String) redisUtil.get(key));
            SmartUser user = JSONObject.toJavaObject(info, SmartUser.class);
            this.lououtPassportByToken(user.getSmartUrl(), user.getSmartToken());
            redisUtil.del(key);
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
        if (schoolVo == null) {
            throw new CommonException(SmartUserErrorEnum.SCHOOL_INFO_NOT_FOUND);
        }

        String school_smart_address = schoolVo.getDomainAddressMapper();
        String school_socket_io_address = schoolVo.getSocketIOAddressMapper();

        school_smart_address = formatIpAddress(school_smart_address);
        school_socket_io_address = formatIpAddress(school_socket_io_address);

        if (isSmartUserExist(smartUser.getOpenId()) && StringUtils.isNotEmpty(smartUser.getSmartToken())) {

            JSONObject resultOfloginByToken = loginPassportByToken(school_smart_address, smartUser.getSmartToken());
            if (resultOfloginByToken != null &&
                    resultOfloginByToken.getInteger("status") == 1) {
                smartUser.setSmartUrl(school_smart_address);
                smartUser.setSockitioUrl(school_socket_io_address);
                return smartUser;
            }
        }

        JSONObject resultOfloginByPwd = loginSmartClassApiByNameAndPwd(school_smart_address, smartUser.getUserName(), smartUser.getPassword());

        if (resultOfloginByPwd != null &&
                resultOfloginByPwd.getBoolean("success") == true) {
            formatSmartUserFromLoginResult(smartUser, school_smart_address, school_socket_io_address, resultOfloginByPwd);
        } else {
            log.error("登录失败. logininfo :" + smartUser + "; result is :" + resultOfloginByPwd);
            throw new CommonException(SmartUserErrorEnum.PASSPORT_LOGIN_FAILED);
        }


        this.saveInDB(smartUser);

        return smartUser;
    }

    /***
     * 根据登录结果设置用户信息
     * @param smartUser
     * @param school_smart_address
     * @param school_socket_io_address
     * @param resultOfloginByToken
     */
    private void formatSmartUserFromLoginResult(SmartUser smartUser, String school_smart_address, String school_socket_io_address, JSONObject resultOfloginByToken) {
        JSONObject resultJson = resultOfloginByToken.getJSONObject("result");

        smartUser.setSmartToken(resultJson.getString("token"));
        int userType = resultJson.getInteger("userType");

        if (userType == UserTypeEnum.STUDENT.getType()) {
            smartUser.setClassName(resultJson.getString("className"));
            smartUser.setGrade(resultJson.getString("grade"));
            smartUser.setSpecialtyName(resultJson.getString("specialtyName"));
            smartUser.setUserId(resultJson.getLong("userId"));
        }
        smartUser.setHeadImageUrl(formatSmartResourceUrl(resultJson.getString("headImageUrl"), school_smart_address));
        smartUser.setSchoolName(resultJson.getString("schoolName"));
        smartUser.setSmartToken(resultJson.getString("token"));
        smartUser.setSmartUrl(school_smart_address);
        smartUser.setSockitioUrl(school_socket_io_address);
    }


    private String formatIpAddress(String ipAddress) {
        if (StringUtils.isNotEmpty(ipAddress) && ipAddress.endsWith("/")) {
            ipAddress = ipAddress.substring(0, ipAddress.length() - 1);
        }
        return ipAddress;
    }

    public String formatSmartResourceUrl(String resourceUrl, String ipAddress) {
        if (StringUtils.isEmpty(resourceUrl)) {
            return "";
        }
        String reg = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"; //匹配ip的正则
        String url = resourceUrl.replaceFirst(reg, ipAddress);
        return url;
    }

    /***
     * 使用token登陆学校的passport
     * @param school_smart_address
     * @param smartToken
     * @return
     */
    private JSONObject loginPassportByToken(String school_smart_address, String smartToken) {
        String loginUrl = PLATFORM_URL.URL_HTTP_PREFIX + school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_PASSPORT_BY_TOKEN;
        HashMap<String, String> loginParms = new HashMap<>();
        loginParms.put("token", smartToken);

        JSONObject resultJson = HttpConnectionUtils.post(loginUrl, loginParms);
        return resultJson;
    }

    /***
     * 使用token登出学校的passport
     * @param school_smart_address
     * @param smartToken
     * @return
     */
    private boolean lououtPassportByToken(String school_smart_address, String smartToken) {
        String loginUrl = PLATFORM_URL.URL_HTTP_PREFIX + school_smart_address + PLATFORM_URL.URL_LOGOUT_SCHOOL_PASSPORT_BY_TOKEN;
        HashMap<String, String> loginParms = new HashMap<>();
        loginParms.put("token", smartToken);

        JSONObject resultJson = HttpConnectionUtils.post(loginUrl, loginParms);
        return resultJson.getBoolean("success");
    }

    /***
     * 使用用户名、密码登陆学校的smartclassapi
     * @param school_smart_address
     * @param userName
     * @param password
     * @return
     */
    private JSONObject loginSmartClassApiByNameAndPwd(String school_smart_address, String userName, String password) {

        try {
            String loginUrl = PLATFORM_URL.URL_HTTP_PREFIX + school_smart_address + PLATFORM_URL.URL_LOGIN_SCHOOL_SMART_CLASS_API;
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
     * 使用用户名、密码登陆学校的passport
     * @param school_smart_address
     * @param userName
     * @param password
     * @return
     */
    private JSONObject loginPassportByNameAndPwd(String school_smart_address, String userName, String password) {

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
