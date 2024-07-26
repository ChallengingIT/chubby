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
                    .requestMatchers("/aziende/**").hasAnyAuthority("ADMIN","BM", "BUSINESS", "RECRUITER", "RIBA")
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

        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}