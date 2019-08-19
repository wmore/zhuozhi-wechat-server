package net.joywise.wechat.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
@Api("课程信息的接口")
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseTeachingService courseTeachingService;


    @ApiOperation(value="添加课程信息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult<CourseTeaching> save(@RequestBody CourseTeaching courseTeaching) {
        ServiceResult result = new ServiceResult(true);

        result.setData(courseTeachingService.createNewCourse(courseTeaching));

        return result;
    }

    @ApiOperation(value="查询课程信息")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResult<CourseTeaching> get(@RequestParam Long snapshotId, @RequestParam Long schoolId) {
        ServiceResult result = new ServiceResult(true);

        result.setData(courseTeachingService.queryBySnapshotId(snapshotId,schoolId));

        return result;
    }

}
