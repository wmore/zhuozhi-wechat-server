package net.joywise.wechat.server.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * @author: wyue
 * @Date: 2019/7/23 14:33
 * @Description:
 */
@Slf4j
public class HttpConnectionUtils {

    public static final int HTTP_CODE_NORMAL = 200;

    private HttpConnectionUtils() {
    }

    /**
     * @Description post 请求封装
     * @Author wyue
     * @Date 2019/7/23 14:33
     * @Param * @param null
     * @Return
     * @Exception
     */
    public static JSONObject post(final String url, JSONObject postJson) {

        if (!StringUtils.isNotBlank(url)) {
            return null;

        }

        log.info("requestJson:{}", postJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(postJson.toString(), headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);// 设置超时
        requestFactory.setReadTimeout(180000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        log.info("URL:{}", url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Integer code = response.getStatusCode().value();
        if (HTTP_CODE_NORMAL != code) {

            //错误返回值处理

        }

        JSONObject body = JSONObject.parseObject(response.getBody());
        log.info("body:{}", body);
        return body;
    }

    public static JSONObject get(String url, Map<String, Object> params) {
        UriComponents uriComponents = HttpConnectionUtils.buildUriComponents(url, params);
        return HttpConnectionUtils.get(uriComponents);
    }

        /**
         * @Description get请求封装
         * @Author wyue
         * @Date 2019/7/23 14:34
         * @Param * @param null
         * @Return
         * @Exception
         */
    public static JSONObject get(final UriComponents uriComponents) {

        if (null == uriComponents) {
            return null;

        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(5000);
        requestFactory.setConnectTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        URI uri = uriComponents.toUri();
        log.info(uri.toString());
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Integer code = response.getStatusCode().value();
        if (200 != code) {

            //错误返回值处理

        }

        JSONObject body = JSONObject.parseObject(response.getBody());
        log.info("body:{}", body);
        return body;

    }

    /**
     * @Description delete请求封装
     * @Author wyue
     * @Date 2019/7/23 14:34
     * @Param * @param null
     * @Return
     * @Exception
     */
    public static JSONObject delete(final String url, final Map<String, Object> map) {

        if (!StringUtils.isNotBlank(url)) {

            return null;

        }

        log.info("URL:{}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);// 设置超时
        requestFactory.setReadTimeout(180000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class, map);
        Integer code = response.getStatusCode().value();

        if (200 != code) {

            //错误返回值处理

        }

        JSONObject body = JSONObject.parseObject(response.getBody());
        log.info("body:{}", body);
        return body;

    }


    public static UriComponents buildUriComponents(String url, Map<String, Object> params) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build().expand(params).encode();
        log.debug("buildUriComponents:", uriComponents);
        return uriComponents;
    }

}
