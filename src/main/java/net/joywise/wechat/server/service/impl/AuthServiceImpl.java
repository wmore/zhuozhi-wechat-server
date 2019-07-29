package net.joywise.wechat.server.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.service.AuthService;
import net.joywise.wechat.server.util.SHA1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
/**
 *  @author: wyue
 *  @Date: 2019/7/23 16:28
 *  @Description:
 */ 
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${com.constant.weixin.token}")
    String token;


    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] str = new String[]{token, timestamp, nonce};
        System.out.println(str);
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }

}
