package net.joywise.wechat.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.db.WechatUser;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import net.joywise.wechat.server.service.SchoolService;
import net.joywise.wechat.server.service.SmartUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@ApiIgnore
@Controller
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @ApiOperation(value="查询机构列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResult<SchoolVo> getSchoolList(@RequestParam Boolean isWechatUsed) {
        ServiceResult result = new ServiceResult(true);

        result.setData(schoolService.getSchoolList(isWechatUsed));

        return result;
    }

}
