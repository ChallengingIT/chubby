package it.challenging.torchy.security;

import it.challenging.torchy.security.jwt.AuthEntryPointJwt;
import it.challenging.torchy.security.jwt.AuthTokenFilter;
import it.challenging.torchy.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    /*private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*http.headers()
            .xssProtection()
            .and()
            .contentSecurityPolicy("script-src 'self'");*/

        http.cors(cors -> {
            CorsConfigurationSource cs = resources -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:80"));
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
            .csrf().disable()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //.and()
            //    .requiresChannel(channel ->                 //remove to return in http mode
            //            channel.anyRequest().requiresSecure())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/associazioni/**").authenticated()
                    .requestMatchers("/azioni/**").authenticated()
                    .requestMatchers("/aziende/**").authenticated()
                    .requestMatchers("/staffing/**").authenticated()
                    .requestMatchers("/files/**").authenticated()
                    .requestMatchers("/calendar/**").authenticated()
                    .requestMatchers("/email/**").authenticated()
                    .requestMatchers("/fornitori/**").authenticated()
                    .requestMatchers("/intervista/**").authenticated()
                    .requestMatchers("/keypeople/**").authenticated()
                    .requestMatchers("/need/**").authenticated()
                    .anyRequest().authenticated()
                    //.anyRequest().permitAll()
            )
            .logout()
                .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
            .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired");

        http.exceptionHandling().accessDeniedPage("/error");

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}