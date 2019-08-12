package net.joywise.wechat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Title: WebCrossConfiguration
 * @Description: 解决CrossOrigin问题
 * @author: wyue
 * @date: 2019/8/12 11:33
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/12 11:33
 * @company: shopin.net
 * @version: V1.0
 */
@Configuration
public class WebCrossConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
    }
}
