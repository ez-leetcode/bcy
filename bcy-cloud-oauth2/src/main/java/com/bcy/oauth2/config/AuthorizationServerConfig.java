package com.bcy.oauth2.config;

import com.bcy.oauth2.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //token持久化
        //配置授权服务策略
        endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService).tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //token前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

        @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端持久化
        //配置网关服务的用户名密码，仅网关服务可作为客户端可访问oauth服务
        clients.inMemory()
                .withClient("bcy-cloud-gateway")
                //密钥
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("password")
                //访问令牌有效期
                .accessTokenValiditySeconds(24 * 3600)
                //刷新令牌有效期
                .refreshTokenValiditySeconds(24 * 3600)
                .scopes("all");
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许客户端发送表单来进行权限认证来获取令牌
        //只允许认证的客户端，比如网关服务才可以获取和校验token
        security//check_token无权限也可以访问 给gateway远程rpc调用
                 .checkTokenAccess("permitAll()")
                 .tokenKeyAccess("permitAll()")
                 .allowFormAuthenticationForClients();
    }

}
