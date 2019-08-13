package net.joywise.wechat.server.error.handler;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.error.CommonException;
import net.joywise.wechat.server.error.WxErrorException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {


    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public ServiceResult errorHandler(CommonException e) {
        log.error("An error condition during the service running.", e);

        ServiceResult result = new ServiceResult(false);
        result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        result.setMessage(e.getMessage());

        return result;
    }

    @ExceptionHandler(value = WxErrorException.class)
    @ResponseBody
    public ServiceResult WXErrorHandler(WxErrorException e) {
        log.error("An error condition during the service call wechat interface.", e);

        ServiceResult result = new ServiceResult(false);
        result.setStatusCode(e.getError().getErrCode());
        result.setMessage(e.getError().toString());

        return result;
    }

}
