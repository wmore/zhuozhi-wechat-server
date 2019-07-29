package net.joywise.wechat.server.service;


public interface AuthService {


    /**
    * @Description 验证微信公众号的签名
    * @Author  wyue
    * @Date   2019/7/23 15:17
    * @Param  * @param null
    * @Return
    * @Exception
    *
    */
    boolean checkSignature(String signature, String timestamp, String nonce);

}
