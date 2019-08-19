package net.joywise.wechat.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.constant.PLATFORM_URL;
import net.joywise.wechat.server.service.SchoolService;
import net.joywise.wechat.server.util.HttpConnectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${com.constant.domain.cloud-base}")
    public String DOMAIN_CLOUD_BASE;


    @Override
    public List<SchoolVo> getSchoolList(boolean isWechatUsed) {
        return null;
    }

    @Override
    public SchoolVo getSchoolById(long schoolId) {
        String url = DOMAIN_CLOUD_BASE + PLATFORM_URL.URL_GET_SCHOOL_INFO;
        Map<String, Object> data = new HashMap<>();
        data.put("school_id", schoolId);
        JSONObject resultJson = HttpConnectionUtils.get(url, data).getJSONObject("data");
        SchoolVo schoolVo = JSONObject.toJavaObject(resultJson, SchoolVo.class);

        return schoolVo;
    }
}
