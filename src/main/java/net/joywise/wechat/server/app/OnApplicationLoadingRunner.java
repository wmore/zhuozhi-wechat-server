package net.joywise.wechat.server.app;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.constant.CACHE_KEY;
import net.joywise.wechat.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Title: OnApplicationLoadingRunner
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/5 14:48
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/5 14:48
 * @company: shopin.net
 * @version: V1.0
 */
@Component
@Slf4j
public class OnApplicationLoadingRunner implements CommandLineRunner {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        clearAllRedisCache();
    }

    private void clearAllRedisCache() {
//        redisUtil.delKeys(CACHE_KEY.ACCESS_TOKEN_KEY);
//        redisUtil.delKeys(CACHE_KEY.OAUTH2_ACCESS_TOKEN_KEY_PREFIX + "*");
//        redisUtil.delKeys(CACHE_KEY.TICKET_KEY_PREFIX + "*");

        log.warn("..OnApplicationLoadingRunner, clearAllRedisCache..");
    }

}
