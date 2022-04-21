package com.example.githuboauth2client.config;

import com.example.githuboauth2client.users.CustomOAuth2Service;
import com.example.githuboauth2client.service.UserService;
import com.example.githuboauth2client.users.CustomUserDetailService;
import com.example.githuboauth2client.utils.CustomAuthenticationSuccessHandler;
import com.example.githuboauth2client.utils.LogOutHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@EnableWebSecurity
@Configuration
@NoArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomOAuth2Service customOAuth2Service;
    private UserService userService;
    private CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomOAuth2Service customOAuth2Service, UserService userService, CustomUserDetailService customUserDetailService) {
        this.customOAuth2Service = customOAuth2Service;
        this.userService = userService;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
        configuration.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/oauth2/**", "/cred").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().deleteCookies("session-spr")
                .addLogoutHandler(new LogOutHandler(objectMapper()))
                .and()
                .formLogin()
                .failureHandler((req, res, auth) -> {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    objectMapper().writeValue(res.getOutputStream(), "Wrong Credentials");
                })
                .successHandler(new CustomAuthenticationSuccessHandler(userService, objectMapper()))
                .and()
                .oauth2Login()
                .successHandler(new CustomAuthenticationSuccessHandler(userService, objectMapper()))
                .userInfoEndpoint()
                .userService(customOAuth2Service);
    }

}
