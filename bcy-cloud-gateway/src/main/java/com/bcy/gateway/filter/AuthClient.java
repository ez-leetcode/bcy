package com.bcy.gateway.filter;

import com.bcy.gateway.utils.RedisUtils;
import com.bcy.pojo.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class AuthClient {

    @Autowired
    private RedisUtils redisUtils;

    private final String checkTokenUrl = "http://47.107.108.55:8006/oauth/check_token";

    private RestTemplate restTemplate = new RestTemplate();


    public boolean hasPermissionControl(String url) {
        return !url.startsWith("/oauth") && !url.startsWith("/websocket");
    }

    public boolean accessable(ServerHttpRequest request) {
        String token = request.getQueryParams().getFirst("token");
        String id = request.getQueryParams().getFirst("id");
        String phone = request.getQueryParams().getFirst("phone");
        long realId = 0;
        if(id != null){
            realId = Long.parseLong(id);
        }
        if(phone != null){
            //关于手机号的直接通过
            return true;
        }
        log.info("token：" + token);
        if(token == null){
            log.warn("用户不带token访问除了oauth以外的服务");
            return false;
        }
        if(token.length() <= 36){
            //密码服务
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(checkTokenUrl).queryParam("token", token);
            URI url = builder.build().encode().toUri();
            HttpEntity<?> entity = new HttpEntity<>(request.getHeaders());
            try {
                ResponseEntity<TokenInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, TokenInfo.class);
                log.info("oauth request: {}, response body: {}, response status: {}",
                        entity, response.getBody(), response.getStatusCode());
                return response.getBody() != null && response.getBody().isActive();
            } catch (RestClientException e) {
                log.error("oauth failed.", e);
                return false;
            }
        }else{
            //redis找token
            String realTokenForWeibo = redisUtils.getValue("3token_" + realId);
            log.info(realTokenForWeibo);
            if(realTokenForWeibo != null && realTokenForWeibo.equals(token)){
                return true;
            }
            String realTokenForSms = redisUtils.getValue("1token_" + realId);
            String realTokenForSms1 = redisUtils.getValue("2token_" + realId);
            log.info(realTokenForSms);
            log.info(realTokenForSms1);
            if(realTokenForSms != null && realTokenForSms.equals(token)){
                return true;
            }
            if(realTokenForSms1 != null && realTokenForSms1.equals(token)){
                return true;
            }
            return false;
        }
    }

}
