package it.challenging.torchy.security;

import it.challenging.torchy.security.jwt.AuthEntryPointJwt;
import it.challenging.torchy.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    private final AuthenticationProvider authenticationProvider;
    private final AuthTokenFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigurerCustomizer = httpSecurityLogoutConfigurer -> {
            httpSecurityLogoutConfigurer.deleteCookies("JSESSIONID");
            httpSecurityLogoutConfigurer.permitAll();
        };

        Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigurerCustomizer =
                httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.maximumSessions(1);

        http
                .csrf(AbstractHttpConfigurer::disable)
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //.and()
            //    .requiresChannel(channel ->                 //remove to return in http mode
            //            channel.anyRequest().requiresSecure())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/auth/delete").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/candidato/auth/**").permitAll()
                    .requestMatchers("/candidato/need/**").hasAuthority("CANDIDATO")
                    .requestMatchers("/candidato/auth/delete").hasAuthority("CANDIDATO")
                    .requestMatchers("/api/auth/change/password").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/ai/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/associazioni/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/azioni/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/aziende/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/job/description/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/dashboard/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/hiring/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/staffing/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/owner/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/files/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/calendar/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/email/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/fornitori/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/intervista/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/keypeople/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .requestMatchers("/need/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
                    .anyRequest().authenticated()

                    //.anyRequest().permitAll()
            )
            .logout(logoutConfigurerCustomizer)
            .sessionManagement(sessionManagementConfigurerCustomizer);

        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}