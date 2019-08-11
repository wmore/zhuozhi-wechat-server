package net.joywise.wechat.server.error.handler;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.ServiceResult;
import net.joywise.wechat.server.error.CommonException;
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
        log.error("", e);

        ServiceResult result = new ServiceResult(false);
        result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        result.setMessage(e.getMessage());

        return result;
    }

}
