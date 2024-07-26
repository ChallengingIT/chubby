package it.challenging.torchy.security;

import it.challenging.torchy.security.jwt.AuthEntryPointJwt;
import it.challenging.torchy.security.jwt.AuthTokenFilter;
import it.challenging.torchy.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigurerCustomizer = httpSecurityLogoutConfigurer -> {
            httpSecurityLogoutConfigurer.deleteCookies("JSESSIONID");
            httpSecurityLogoutConfigurer.permitAll();
        };

        Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigurerCustomizer = httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.maximumSessions(1);

        Customizer<CsrfConfigurer<HttpSecurity>> csrfConfigurerCustomizer = AbstractHttpConfigurer::disable;

        http.cors(cors -> {
            CorsConfigurationSource cs = resources -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(List.of("Authorization",
                    "Content-Type",
                    "X-Requested-With",
                    "Accept",
                    "X-XSRF-TOKEN"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            };

            cors.configurationSource(cs);
        });

        http
            .csrf(csrfConfigurerCustomizer)
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //.and()
            //    .requiresChannel(channel ->                 //remove to return in http mode
            //            channel.anyRequest().requiresSecure())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/auth/delete").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/api/auth/mapp/**").permitAll()
                    .requestMatchers("/api/auth/mapp/delete").authenticated()
                    .requestMatchers("/candidato/auth/**").permitAll()
                    .requestMatchers("/candidato/need/**").hasRole("CANDIDATO")
                    .requestMatchers("/candidato/auth/delete").hasRole("CANDIDATO")
                    .requestMatchers("/church/**").authenticated()
                    .requestMatchers("/api/auth/change/password").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/ai/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/associazioni/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/qr/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/azioni/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/aziende/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/job/description/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/dashboard/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/hiring/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/staffing/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/owner/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/files/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/calendar/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/email/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/fornitori/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/intervista/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/keypeople/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/need/**").hasAnyRole("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .anyRequest().authenticated()
                    //.anyRequest().permitAll()
            )
            .logout(logoutConfigurerCustomizer)
            .sessionManagement(sessionManagementConfigurerCustomizer);

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}