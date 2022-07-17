package com.fds.rest.config;

import com.fds.rest.security.jwt.JwtConfigurer;
import com.fds.rest.security.oauth2.CustomOAuth2User;
import com.fds.rest.security.oauth2.CustomOAuth2UserService;
import com.fds.rest.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String SIGN_UP_ENDPOINT = "/api/v1/auth/signup";
    private static final String OAUTH2_ENDPOINT = "/login/oauth2/**";
    private final JwtConfigurer jwtConfigurer;

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private UserServiceImpl userService;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT, SIGN_UP_ENDPOINT, OAUTH2_ENDPOINT)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfigurer)
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                    userService.processOAuthPostLogin(oauthUser.getEmail());

                    response.sendRedirect("/api/v1/auth/success");
                });
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
