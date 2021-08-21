package com.bcy.gateway.filter;

import com.bcy.gateway.utils.RedisUtils;
import com.bcy.pojo.TokenInfo;
import com.bcy.utils.BloomFilterUtils;
import com.bcy.utils.BngelUtils;
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

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    private final String checkTokenUrl = "http://47.107.108.55:8006/oauth/check_token";

    private RestTemplate restTemplate = new RestTemplate();


    public boolean hasPermissionControl(String url) {
        return !url.startsWith("/oauth") && !url.startsWith("/websocket");
    }

    public boolean accessable(ServerHttpRequest request) {
        String token = request.getQueryParams().getFirst("token");
        String id = request.getQueryParams().getFirst("id");
        String phone = request.getQueryParams().getFirst("phone");
        if(id != null && id.equals("0")){
            return true;
        }
        String id1 = null;
        long realId = 0;
        if(id != null){
            id1 = BngelUtils.getRealFileName(id);
            realId = Long.parseLong(id1);
        }
        //用布隆过滤器判断是否为恶意id
        if(id1 != null && !redisUtils.includeByBloomFilter(bloomFilterUtils,"bcy",id1)){
            log.info(id1);
            log.error("布隆过滤器检测到恶意id，正在过滤（暂时不拦截了）");
            return true;
        }
        if(phone != null){
            //关于手机号的直接通过
            return true;
        }
        //如果id不存在 直接通过
        if(id == null){
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
            return realTokenForSms1 != null && realTokenForSms1.equals(token);
        }
    }

}
