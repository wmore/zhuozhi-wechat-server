package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.wechat.QrCode;

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
public interface QrService {

    /***
     * 获取临时带参二维码
     * expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * scene_str	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return
     */
    QrCode getTempQrTicketWithScene(int expireSeconds, String scene);

    /***
     * 获取永久带参二维码
     * @return
     */
    QrCode getQrTicketWithScene(String scene);


}
