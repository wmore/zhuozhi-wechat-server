package net.joywise.wechat.server.controller;

import io.swagger.annotations.Api;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.bean.wechat.QrCode;
import net.joywise.wechat.server.service.MsgService;
import net.joywise.wechat.server.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Api("一句话描述文档说明")
@Controller
@RequestMapping("/qrcode")
public class QrcodeController {
    @Autowired
    private QrService qrService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult getTemQrCodeWithScene(Integer expire_seconds, String scene_str) {
        ServiceResult serviceResult = new ServiceResult(true);
        if (expire_seconds == null){
            expire_seconds = 60;
        }
        QrCode qrCode = qrService.getTempQrTicketWithScene(expire_seconds, scene_str);
        serviceResult.setData(qrCode);
        return serviceResult;
    }


    @RequestMapping(value = "/create_limit", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult getQrCodeWithScene(String scene_str) {
        ServiceResult serviceResult = new ServiceResult(true);
        QrCode qrCode = qrService.getQrTicketWithScene(scene_str);
        serviceResult.setData(qrCode);
        return serviceResult;
    }

}
