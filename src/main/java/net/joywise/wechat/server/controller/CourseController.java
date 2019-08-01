package net.joywise.wechat.server.controller;

import io.swagger.annotations.Api;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Title: MsgController
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:33
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:33
 * @company: shopin.net
 * @version: V1.0
 */
@Api("一句话描述文档说明")
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseTeachingService courseTeachingService;


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult save(@RequestBody CourseTeaching courseTeaching) {
        ServiceResult result = new ServiceResult(true);

        result.setData(courseTeachingService.createNewCourse(courseTeaching));

        return result;
    }

}
