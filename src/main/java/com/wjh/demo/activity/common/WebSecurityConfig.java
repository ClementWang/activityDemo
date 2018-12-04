package com.wjh.demo.activity.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf保护功能（跨域访问）
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**").permitAll() //访问API下无需登录认证权限
            .and()
            .formLogin().and()
            .httpBasic();
    }
}
