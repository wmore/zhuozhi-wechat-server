package net.joywise.wechat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Title: SwaggerConfiguration
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/7/29 11:22
 * @最后修改人: Administrator
 * @最后修改时间: 2019/7/29 11:22
 * @company: shopin.net
 * @version: V1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.joywise.wechat.server.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微信h5后端接口 RESTful APIs")
                .description("微信h5后端接口文档")
                .termsOfServiceUrl("http://localhost:8088/wechat-server")
                .version("1.0")
                .build();
    }
}