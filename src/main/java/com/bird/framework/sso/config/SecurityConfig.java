package com.bird.framework.sso.config;

import com.bird.framework.sso.security.CustomAccessDeniedHandler;
import com.bird.framework.sso.security.CustomAuthenticationFilter;
import com.bird.framework.sso.security.CustomLoginHandler;
import com.bird.framework.sso.security.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomLoginHandler customLoginHandler;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll();

        http.formLogin();

        http.logout()
                .logoutUrl("/logout")
                // 登出前调用，可用于日志
                .addLogoutHandler(customLogoutHandler)
                // 登出后调用，用户信息已不存在
                .logoutSuccessHandler(customLogoutHandler);

        http.exceptionHandling()
                // 已登入用户的权限错误
                .accessDeniedHandler(customAccessDeniedHandler)
                // 未登入用户的权限错误
                .authenticationEntryPoint(customAccessDeniedHandler);

        http.csrf().disable();

        // 根据 Header Accept-Language 字段设置 Locale
        // 要想启用错误信息的本地化，还需要设置MessageSource，请参阅Github源码
//        http.addFilterBefore(new AcceptHeaderLocaleFilter(), UsernamePasswordAuthenticationFilter.class);

        // 替换原先的表单登入 Filter
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(customLoginHandler);
        filter.setAuthenticationFailureHandler(customLoginHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        return filter;
    }
}
