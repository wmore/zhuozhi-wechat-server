package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.constant.PLATFORM_URL;
import net.joywise.wechat.server.service.SchoolService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import org.springframework.stereotype.Service;

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
@Service
public class SchoolServiceImpl implements SchoolService {

    @Override
    public List<SchoolVo> getSchoolList(boolean isWechatUsed) {
        return null;
    }

    @Override
    public SchoolVo getSchoolById(long schoolId) {
//        String url = PLATFORM_URL.URL_GET_SCHOOL_INFO.replace("{school_id}", String.valueOf(schoolId));
//        JSONObject resultJson = HttpConnectionUtils.get(PLATFORM_URL.DOMAIN_CLOUD_BASE + url, null);

        JSONObject resultJson = new JSONObject();
        resultJson.put("schoolId", 93);
        resultJson.put("schoolName", "福建师范大学");
        resultJson.put("schoolType", 1);
        resultJson.put("isWechatUsed", true);
        resultJson.put("domainAddressMapper", "10.10.23.31");
        resultJson.put("socketIOAddressMapper", "10.10.23.31");

        SchoolVo schoolVo = JSON.toJavaObject(resultJson, SchoolVo.class);

        return schoolVo;
    }
}
