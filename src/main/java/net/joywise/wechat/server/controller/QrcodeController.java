package net.joywise.wechat.server.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.service.JSAPITicketService;
import net.joywise.wechat.server.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Title: QrcodeController
 * @Description: 二维码相关接口
 * @author: wyue
 * @date: 2019/7/29 11:33
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:33
 * @company: shopin.net
 * @version: V1.0
 */
@Api("二维码相关")
@Controller
@RequestMapping("/qrcode")
public class QrcodeController {
    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private JSAPITicketService jsapiTicketService;

    @ApiOperation(value = "创建一个临时二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "expire_seconds",
                    value = "该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "scene_str",
                    value = "二维码参数，场景值ID（字符串形式的ID），字符串类型，长度限制为1到64",
                    dataType = "String", paramType = "query")}
    )
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult<QrCode> getTemQrCodeWithScene(Integer expire_seconds, String scene_str) {
        ServiceResult serviceResult = new ServiceResult(true);
        if (expire_seconds == null) {
            expire_seconds = 60;
        }
        QrCode qrCode = qrcodeService.getTempQrTicketWithScene(expire_seconds, scene_str);
        serviceResult.setData(qrCode);
        return serviceResult;
    }

    @ApiOperation(value = "创建一个永久二维码")
    @ApiImplicitParam(name = "scene_str",
            value = "二维码参数，场景值ID（字符串形式的ID），字符串类型，长度限制为1到64",
            dataType = "String", paramType = "query")
    @RequestMapping(value = "/create_limit", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult<QrCode> getQrCodeWithScene(String scene_str) {
        ServiceResult serviceResult = new ServiceResult(true);
        QrCode qrCode = qrcodeService.getQrTicketWithScene(scene_str);
        serviceResult.setData(qrCode);
        return serviceResult;
    }

    @ApiOperation(value = "获取二维码")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResult<QrCode> getQrCodeWithScene(@RequestParam Long schoolId, @RequestParam Long snapshotId) {
        ServiceResult serviceResult = new ServiceResult(true);
        String sceneStr = schoolId + "_" + snapshotId;
        QrCode qrCode = qrcodeService.getQrTicketWithScene(sceneStr);
        serviceResult.setData(qrCode);
        return serviceResult;
    }

    @ApiOperation(value = "获取签名，用于前端页面调用微信jsapi")
    @RequestMapping(value = "/get_sign", method = RequestMethod.GET)
    @ResponseBody
    public String getSign(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String domainUrl = request.getRequestURL().toString();
        String qrcodeUrl = domainUrl.replace("get_sign", "scan");
        System.out.println(qrcodeUrl);
        return JSON.toJSONString(jsapiTicketService.getSign(qrcodeUrl));
    }

    @RequestMapping("/scan")
    public String doScan(Model model) {
        return "scan_qrcode";
    }

}
