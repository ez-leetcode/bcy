package com.bcy.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //针对于对gateway本身的请求直接放行
        http.authorizeRequests().antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .and().csrf().disable();
    }

}
