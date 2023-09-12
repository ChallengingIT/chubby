/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.headers()
            .xssProtection()
            .and()
            .contentSecurityPolicy("script-src 'self'");

        http
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .requiresChannel(channel ->
                channel.anyRequest().requiresSecure())
            .authorizeHttpRequests()
                .requestMatchers( "/resources/**", "/css/**", "/js/**","/images/**","/static/**").permitAll()
            .requestMatchers("/**","/homepage/**","/staffing/**","/bacheca/**", "/need/**", "/associazioni/**",
                "/fornitori/**","/intervista/**", "/progetti/**", "/hr/**" , "/user/**","/aziende/**","/keypeople/**",
                "/fatturazione/**","/scheda/**")
                .hasAnyRole( "ADMIN","USER","BM","RECRUITER")
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/homepage")
                .permitAll()
                .and()
            .logout()
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .sessionManagement()
                .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl("/login?expired");

        http.exceptionHandling().accessDeniedPage("/error");

        return http.build();
    }
}