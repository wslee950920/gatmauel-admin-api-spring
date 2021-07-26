package com.gatmauel.admin.config.security;

import com.gatmauel.admin.config.security.filter.ApiLoginFilter;
import com.gatmauel.admin.config.security.handler.ApiLoginFailHandler;
import com.gatmauel.admin.config.security.handler.ApiLoginSuccessHandler;
import com.gatmauel.admin.config.security.handler.ApiLogoutSuccessHandler;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    LogoutSuccessHandler logoutSuccessHandler(){
        return new ApiLogoutSuccessHandler();
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{
        ApiLoginFilter apiLoginFilter=new ApiLoginFilter("/@admin/auth/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
        apiLoginFilter.setAuthenticationSuccessHandler(new ApiLoginSuccessHandler());

        return apiLoginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/@admin/category/list").permitAll()
                .antMatchers("/@admin/food/list").permitAll()
                .antMatchers("/@admin/auth/register").permitAll()
                .antMatchers("/@admin/auth/confirm").permitAll()
                .anyRequest().hasRole("ADMIN");
        http.formLogin().disable();
        http.csrf().disable();
        http.logout()
                .logoutUrl("/@admin/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler());

        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
