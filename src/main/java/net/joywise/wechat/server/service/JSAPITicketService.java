package net.joywise.wechat.server.service;

import net.joywise.wechat.server.enums.TicketTypeEnum;

import java.util.Map;

/**
 * @Title: JSAPITicketService
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 14:07
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 14:07
 * @company: shopin.net
 * @version: V1.0
 */
public interface JSAPITicketService {

    void saveTicket(String ticket, int expiresIn, TicketTypeEnum ticketTypeEnum);

    String getTicket(String accessToken, TicketTypeEnum ticketTypeEnum);

    Map<String, String> getSign(String url);

}
